package chat.gptalk.gateway.config;

import chat.gptalk.common.exception.CommonErrorCode;
import chat.gptalk.common.exception.LLMError;
import chat.gptalk.common.exception.LLMError.ErrorDetails;
import chat.gptalk.common.exception.UnauthorizedException;
import chat.gptalk.common.security.ApiAuthenticatedUser;
import chat.gptalk.common.security.ConsoleAuthenticatedUser;
import chat.gptalk.common.security.SecurityConstants;
import chat.gptalk.common.util.JsonUtils;
import chat.gptalk.gateway.security.InternalAuthenticationToken;
import chat.gptalk.gateway.security.JwksManager;
import chat.gptalk.gateway.security.OpenApiAuthenticationToken;
import chat.gptalk.gateway.service.AuthService;
import chat.gptalk.gateway.util.JwtUtils;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPublicKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
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
    private final JwtUtils jwtUtils;
    private final String[] permitAllPaths = {
        "/v1/auth/login",
        "/webjars/**",
        "/swagger**/**",
        "/v3/**",
    };

    public SecurityConfig(AuthService authService, JwksManager jwksManager) {
        this.authService = authService;
        RSAPublicKey publicKey = jwksManager.getPublicKey();
        this.jwtUtils = new JwtUtils(publicKey);
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
            .code(CommonErrorCode.UNAUTHORIZED.errorCode())
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
            String jwtToken = token.getCredentials().toString();
            ConsoleAuthenticatedUser consoleUser = jwtUtils.verifyAndParse(jwtToken);
            return authService.verify(consoleUser.tenantId().toString(), token.getApiKeyId())
                .filter(it -> it)
                .map(it -> new InternalAuthenticationToken(ApiAuthenticatedUser.builder()
                    .userId(consoleUser.userId().toString())
                    .tenantId(consoleUser.tenantId().toString())
                    .apiKeyId(token.getApiKeyId())
                    .build()));
        };
    }

    @Bean
    public ServerAuthenticationConverter authenticationConverter() {
        return this::processAuthorizationToken;
    }

    private Mono<Authentication> processAuthorizationToken(ServerWebExchange exchange) {
        String apiKey = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(apiKey)) {
            return Mono.error(new UnauthorizedException(CommonErrorCode.TOKEN_EMPTY));
        }
        if (!apiKey.startsWith(SecurityConstants.BEARER_PREFIX)) {
            return Mono.error(new UnauthorizedException(CommonErrorCode.UNAUTHORIZED_INVALID_TOKEN_FORMAT));
        }
        String authKey = apiKey.substring(SecurityConstants.BEARER_PREFIX.length());
        String clientId = exchange.getRequest().getHeaders().getFirst(SecurityConstants.HEADER_CLIENT_ID);
        String apiKeyId = exchange.getRequest().getHeaders().getFirst(SecurityConstants.HEADER_API_KEY_ID);
        if (StringUtils.hasText(clientId)) {
            return Mono.just(new InternalAuthenticationToken(authKey, apiKeyId));
        }
        return Mono.just(new OpenApiAuthenticationToken(authKey));
    }
}
