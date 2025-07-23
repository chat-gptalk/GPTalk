package chat.gptalk.connector.config;

import static chat.gptalk.connector.util.ReactiveUtils.ensureTrue;

import chat.gptalk.common.constants.ErrorCode;
import chat.gptalk.common.exception.LLMError;
import chat.gptalk.common.exception.LLMError.ErrorDetails;
import chat.gptalk.common.util.JsonUtils;
import chat.gptalk.connector.security.AuthService;
import chat.gptalk.connector.security.InternalAuthenticationToken;
import chat.gptalk.connector.security.OpenApiAuthenticationToken;
import chat.gptalk.connector.security.OpenApiUser;
import chat.gptalk.security.JwksManager;
import chat.gptalk.security.SecurityConstants;
import chat.gptalk.security.model.AuthUser;
import chat.gptalk.security.util.JwtVerifyUtils;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.FormLoginSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.HttpBasicSpec;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final AuthService authService;
    private final JwtVerifyUtils jwtVerifyUtils;
    private final String[] permitAllPaths = {
        "/v1/auth/login",
        "/webjars/**",
        "/swagger**/**",
        "/v3/**",
    };

    public SecurityConfig(AuthService authService, JwksManager jwksManager) {
        this.authService = authService;
        RSAPublicKey publicKey = jwksManager.loadJwksPublicKey();
        this.jwtVerifyUtils = new JwtVerifyUtils(publicKey);
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
            .csrf(CsrfSpec::disable)
            .httpBasic(HttpBasicSpec::disable)
            .formLogin(FormLoginSpec::disable)
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            .authorizeExchange(it -> it.pathMatchers(permitAllPaths).permitAll().anyExchange().authenticated())
            .addFilterAt(authenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
            .build();
    }

    private Mono<Void> unauthorizedResponse(WebFilterExchange exchange, String message) {
        return writeJsonResponse(exchange, message);
    }

    private Mono<Void> writeJsonResponse(WebFilterExchange exchange, String message) {
        ServerHttpResponse response = exchange.getExchange().getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        LLMError llmError = new LLMError(ErrorDetails.builder()
            .code(ErrorCode.AU_UNAUTHORIZED.name())
            .message(message)
            .build());
        byte[] bytes = JsonUtils.toJson(llmError).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }

    private AuthenticationWebFilter authenticationFilter() {
        ServerWebExchangeMatcher matcher = ServerWebExchangeMatchers.pathMatchers(permitAllPaths);
        AuthenticationWebFilter filter = new AuthenticationWebFilter(authenticationManager());
        filter.setRequiresAuthenticationMatcher(new NegatedServerWebExchangeMatcher(matcher));
        filter.setServerAuthenticationConverter(authenticationConverter());
        filter.setAuthenticationFailureHandler((exchange, ex) -> unauthorizedResponse(exchange, ex.getMessage()));
        return filter;
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        return authentication -> {
            if (authentication instanceof OpenApiAuthenticationToken openApiAuthenticationToken) {
                String apiKey = openApiAuthenticationToken.getCredentials().toString();
                return authService.verify(apiKey)
                    .map(it -> new OpenApiAuthenticationToken(it));
            }
            InternalAuthenticationToken token = (InternalAuthenticationToken) authentication;
            if (!(token.getCredentials() instanceof Map<?, ?>)) {
                log.info("Authentication token does not contain an OpenApi token");
                return Mono.error(new BadCredentialsException("Bad credentials"));
            }
            Map<String, Object> credentials = (Map<String, Object>) token.getCredentials();
            String jwtToken = credentials.get("jwtToken").toString();
            UUID apiKeyId = (UUID) credentials.get("apiKeyId");
            AuthUser adminUser = jwtVerifyUtils.verifyAndParse(jwtToken);
            return ensureTrue(authService.verifyApiKeyOwnership(adminUser.tenantId(), apiKeyId),
                () -> new InternalAuthenticationToken(OpenApiUser.builder()
                    .userId(adminUser.userId())
                    .tenantId(adminUser.tenantId())
                    .apiKeyId(apiKeyId)
                    .build()),
                new BadCredentialsException("API Key not owned by user"));
        };
    }

    @Bean
    public ServerAuthenticationConverter authenticationConverter() {
        return this::processAuthorizationToken;
    }

    private Mono<Authentication> processAuthorizationToken(ServerWebExchange exchange) {
        String apiKey = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(apiKey)) {
            return processCookieAuth(exchange);
        }
        if (!apiKey.startsWith(SecurityConstants.BEARER_PREFIX)) {
            return Mono.error(new BadCredentialsException("Invalid Bearer token"));
        }
        String authKey = apiKey.substring(SecurityConstants.BEARER_PREFIX.length());
        return Mono.just(new OpenApiAuthenticationToken(authKey));
    }

    private Mono<Authentication> processCookieAuth(ServerWebExchange exchange) {
        HttpCookie accessTokenCookie = exchange.getRequest().getCookies().getFirst(SecurityConstants.ACCESS_TOKEN);
        if (accessTokenCookie == null) {
            return Mono.error(new BadCredentialsException("Invalid access token"));
        }
        String apiKeyId = exchange.getRequest().getHeaders().getFirst(SecurityConstants.HEADER_API_KEY_ID);
        if (!StringUtils.hasText(apiKeyId)) {
            return Mono.error(new BadCredentialsException("apiKey ID is required"));
        }
        return Mono.just(new InternalAuthenticationToken(accessTokenCookie.getValue(), apiKeyId));
    }
}
