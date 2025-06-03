package chat.gptalk.gateway.ratelimit;

import chat.gptalk.common.security.SecurityConstants;
import chat.gptalk.gateway.util.GatewayResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class TpmRateLimiterGatewayFilterFactory extends
    AbstractGatewayFilterFactory<TpmRateLimiterGatewayFilterFactory.Config> {

    private final ReactiveStringRedisTemplate redisTemplate;

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String apiKey = exchange.getRequest().getHeaders().getFirst(SecurityConstants.HEADER_API_KEY_ID);
            if (!StringUtils.hasText(apiKey)) {
                return GatewayResponseUtils.unauthorized(exchange);
            }
            String bucketKey = "bucket:" + apiKey;
            return redisTemplate.opsForValue().get(bucketKey)
                .map(Integer::parseInt)
                .defaultIfEmpty(0)
                .flatMap(token -> {
                    if (token < 1) {
                        return GatewayResponseUtils.tooManyRequests(exchange);
                    }
                    return chain.filter(exchange);
                });
        };
    }

    public static class Config {

    }
}
