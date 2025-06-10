package chat.gptalk.gateway.filter;

import chat.gptalk.common.security.ApiAuthenticatedUser;
import chat.gptalk.common.security.SecurityConstants;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ForwardAuthHeaderGatewayFilterFactory extends
    AbstractGatewayFilterFactory<ForwardAuthHeaderGatewayFilterFactory.Config> {

    public ForwardAuthHeaderGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(ForwardAuthHeaderGatewayFilterFactory.Config config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                return ReactiveSecurityContextHolder.getContext()
                    .map(SecurityContext::getAuthentication)
                    .map(it -> (ApiAuthenticatedUser) it.getPrincipal())
                    .flatMap(user -> {
                        ServerHttpRequest httpRequest = exchange.getRequest().mutate()
                            .header(SecurityConstants.HEADER_TENANT_ID, user.tenantId().toString())
                            .header(SecurityConstants.HEADER_USER_ID, user.userId().toString())
                            .header(SecurityConstants.HEADER_API_KEY_ID, user.apiKeyId().toString())
                            .build();
                        return chain.filter(exchange.mutate().request(httpRequest).build());
                    });
            }
        };
    }

    public static class Config {

    }
}
