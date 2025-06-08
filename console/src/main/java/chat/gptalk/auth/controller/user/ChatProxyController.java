package chat.gptalk.auth.controller.user;

import static chat.gptalk.common.security.SecurityConstants.HEADER_API_KEY_ID;
import static chat.gptalk.common.security.SecurityConstants.HEADER_CLIENT_ID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/chat")
@RequiredArgsConstructor
public class ChatProxyController {

    private final WebClient gatewayClient;

    @PostMapping("/completions")
    public Mono<ResponseEntity<?>> proxyChatCompletion(@RequestBody String body,
        @RequestHeader(value = HEADER_API_KEY_ID, required = false) String apiKeyId) {
        //todo webflux to mvc
        return gatewayClient.post()
            .uri("/v1/chat/completions")
            .headers(headers -> {
                //todo
                headers.set(HEADER_API_KEY_ID, "0f911a38-30a9-446c-9aef-f2d48779026e");
                headers.set(HEADER_CLIENT_ID, "gptalk-console");
            })
            .body(body, String.class)
            .exchangeToMono(response -> {
                HttpStatusCode status = response.statusCode();
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.putAll(response.headers().asHttpHeaders());

                if (MediaType.TEXT_EVENT_STREAM.equals(response.headers().contentType().orElse(null))) {
                    return Mono.just(ResponseEntity.status(status)
                        .headers(responseHeaders)
                        .contentType(MediaType.TEXT_EVENT_STREAM)
                        .body(response.bodyToFlux(String.class)));
                }

                return response.bodyToMono(String.class)
                    .map(resBody -> ResponseEntity.status(status)
                        .headers(responseHeaders)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(resBody));
            });
    }
}