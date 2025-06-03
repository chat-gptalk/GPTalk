package chat.gptalk.gateway.ratelimit;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.setResponseStatus;

import java.util.Map;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.HasRouteId;
import org.springframework.cloud.gateway.support.HttpStatusHolder;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class RpmRateLimiterGatewayFilterFactory extends
    AbstractGatewayFilterFactory<RpmRateLimiterGatewayFilterFactory.Config> {

    private static final String EMPTY_KEY = "____EMPTY_KEY__";

    private final RateLimiter rateLimiter;

    private final KeyResolver keyResolver;

    /**
     * HttpStatus to return when denyEmptyKey is true, defaults to FORBIDDEN.
     */
    private final String emptyKeyStatusCode = HttpStatus.FORBIDDEN.name();

    public RpmRateLimiterGatewayFilterFactory(RedisRateLimiter rateLimiter, KeyResolver modelKeyResolver) {
        super(Config.class);
        this.rateLimiter = rateLimiter;
        this.keyResolver = modelKeyResolver;
    }

    @SuppressWarnings("unchecked")
    @Override
    public GatewayFilter apply(Config config) {
        KeyResolver resolver = keyResolver;
        RateLimiter<Object> limiter = rateLimiter;
        HttpStatusHolder emptyKeyStatus = HttpStatusHolder
            .parse(this.emptyKeyStatusCode);

        return (exchange, chain) -> resolver.resolve(exchange).defaultIfEmpty(EMPTY_KEY).flatMap(key -> {
            if (EMPTY_KEY.equals(key)) {
                setResponseStatus(exchange, emptyKeyStatus);
                return exchange.getResponse().setComplete();
            }
            String routeId = config.getRouteId();
            if (routeId == null) {
                Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
                routeId = route.getId();
            }
            return limiter.isAllowed(routeId, key).flatMap(response -> {

                for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
                    exchange.getResponse().getHeaders().add(header.getKey(), header.getValue());
                }

                if (response.isAllowed()) {
                    return chain.filter(exchange);
                }

                setResponseStatus(exchange, HttpStatus.TOO_MANY_REQUESTS);
                return exchange.getResponse().setComplete();
            });
        });
    }

    public static class Config implements HasRouteId {

        private String routeId;

        @Override
        public void setRouteId(String routeId) {
            this.routeId = routeId;
        }

        @Override
        public String getRouteId() {
            return this.routeId;
        }

    }
}
