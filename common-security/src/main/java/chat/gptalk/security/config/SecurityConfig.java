package chat.gptalk.security.config;

import chat.gptalk.common.exception.CommonErrorCode;
import chat.gptalk.common.exception.UnauthorizedException;
import chat.gptalk.common.security.ApiAuthenticatedUser;
import chat.gptalk.common.security.SecurityConstants;
import chat.gptalk.security.security.GatewayAuthenticationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
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
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@EnableWebFluxSecurity
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
            .csrf(CsrfSpec::disable)
            .httpBasic(HttpBasicSpec::disable)
            .formLogin(FormLoginSpec::disable)
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            .authorizeExchange(it -> it.anyExchange().authenticated())
            .addFilterAt(authenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
            .build();
    }

    private AuthenticationWebFilter authenticationFilter() {
        AuthenticationWebFilter filter = new AuthenticationWebFilter(authenticationManager());
        filter.setServerAuthenticationConverter(authenticationConverter());
        return filter;
    }

    private ReactiveAuthenticationManager authenticationManager() {
        return authentication -> {
            try {
                return Mono.just(authentication);
            } catch (Exception e) {
                log.info("auth error: {}", e.getMessage());
                return Mono.error(new UnauthorizedException());
            }
        };
    }

    private ServerAuthenticationConverter authenticationConverter() {
        return this::processGatewayAuthorizationToken;
    }

    private Mono<Authentication> processGatewayAuthorizationToken(ServerWebExchange exchange) {
        String apiKeyId = exchange.getRequest().getHeaders().getFirst(SecurityConstants.HEADER_API_KEY_ID);
        String tenantId = exchange.getRequest().getHeaders().getFirst(SecurityConstants.HEADER_TENANT_ID);
        String userId = exchange.getRequest().getHeaders().getFirst(SecurityConstants.HEADER_USER_ID);
        if (!StringUtils.hasText(apiKeyId)
            || !StringUtils.hasText(tenantId)
            || !StringUtils.hasText(userId)
        ) {
            return Mono.error(new UnauthorizedException(CommonErrorCode.TOKEN_EMPTY));
        }
        return Mono.just(new GatewayAuthenticationToken(ApiAuthenticatedUser.builder()
            .apiKeyId(apiKeyId)
            .tenantId(tenantId)
            .userId(userId)
            .build()));
    }
}
