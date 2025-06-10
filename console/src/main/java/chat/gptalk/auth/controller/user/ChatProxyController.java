package chat.gptalk.auth.controller.user;

import static chat.gptalk.common.security.SecurityConstants.ACCESS_TOKEN;
import static chat.gptalk.common.security.SecurityConstants.HEADER_API_KEY_ID;
import static chat.gptalk.common.security.SecurityConstants.HEADER_CLIENT_ID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/chat")
@RequiredArgsConstructor
public class ChatProxyController {

    private final WebClient gatewayClient;

    @PostMapping("/completions")
    public Mono<ResponseEntity<?>> proxyChatCompletion(@RequestBody String body,
        @CookieValue(ACCESS_TOKEN) String accessToken,
        @RequestHeader(value = HEADER_API_KEY_ID, required = false) String apiKeyId) {
        //todo webflux to mvc
        return gatewayClient.post()
            .uri("/v1/chat/completions")
            .headers(headers -> {
                //todo
                headers.set(HEADER_API_KEY_ID, "fa948168-fdfd-4b2e-adb5-8f2a06e5f3cd");
                headers.set(HEADER_CLIENT_ID, "gptalk-console");
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
            })
            .body(BodyInserters.fromValue(body))
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