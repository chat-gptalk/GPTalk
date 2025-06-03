package chat.gptalk.gateway.util;

import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class GatewayResponseUtils {

    public static Mono<Void> unauthorized(ServerWebExchange exchange) {
        return writeJsonResponse(exchange, HttpStatus.UNAUTHORIZED, "Unauthorized: Missing or invalid API key");
    }

    public static Mono<Void> tooManyRequests(ServerWebExchange exchange) {
        return writeJsonResponse(exchange, HttpStatus.TOO_MANY_REQUESTS, "Too many requests: Rate limit exceeded");
    }

    private static Mono<Void> writeJsonResponse(ServerWebExchange exchange, HttpStatus status, String message) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        byte[] bytes = ("{\"code\":" + status.value() + ",\"message\":\"" + message + "\"}")
            .getBytes(StandardCharsets.UTF_8);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
            .bufferFactory().wrap(bytes)));
    }
}
