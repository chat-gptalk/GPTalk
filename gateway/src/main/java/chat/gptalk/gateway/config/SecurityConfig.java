package chat.gptalk.gateway.config;

import chat.gptalk.common.exception.CommonErrorCode;
import chat.gptalk.common.exception.UnauthorizedException;
import chat.gptalk.common.security.SecurityConstants;
import chat.gptalk.gateway.security.OpenApiAuthenticationToken;
import chat.gptalk.gateway.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final AuthService authService;
    private final String[] permitAllPaths = {
        "/v1/auth/login",
        "/webjars/**",
        "/swagger**/**",
        "/v3/**",
    };
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

    private AuthenticationWebFilter authenticationFilter() {
        ServerWebExchangeMatcher matcher = ServerWebExchangeMatchers.pathMatchers(permitAllPaths);
        AuthenticationWebFilter filter = new AuthenticationWebFilter(authenticationManager());
        filter.setRequiresAuthenticationMatcher(new NegatedServerWebExchangeMatcher(matcher));
        filter.setServerAuthenticationConverter(authenticationConverter());
        return filter;
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        return authentication -> {
            OpenApiAuthenticationToken token = (OpenApiAuthenticationToken) authentication;
            try {
                return authService.verify(token.getCredentials().toString())
                    .map(it -> new OpenApiAuthenticationToken(it));
            } catch (Exception e) {
                log.info("auth error: {}", e.getMessage());
                return Mono.error(new UnauthorizedException());
            }
        };
    }

    @Bean
    public ServerAuthenticationConverter authenticationConverter() {
        return this::processAuthorizationToken;
    }

    private Mono<Authentication> processAuthorizationToken(ServerWebExchange exchange) {
        String jwtToken = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (!StringUtils.hasText(jwtToken)) {
            return Mono.error(new UnauthorizedException(CommonErrorCode.TOKEN_EMPTY));
        }
        if (!jwtToken.startsWith(SecurityConstants.BEARER_PREFIX)) {
            return Mono.error(new UnauthorizedException(CommonErrorCode.UNAUTHORIZED_INVALID_TOKEN_FORMAT));
        }
        return Mono.just(new OpenApiAuthenticationToken(jwtToken.substring(SecurityConstants.BEARER_PREFIX.length())));
    }
}
