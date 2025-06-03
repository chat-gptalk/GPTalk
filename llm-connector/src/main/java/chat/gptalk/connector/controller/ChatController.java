package chat.gptalk.connector.controller;

import chat.gptalk.connector.service.ChatService;
import chat.gptalk.connector.sp.model.chat.ChatCompletionRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("v1")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("chat/completions")
    public Mono<ResponseEntity<?>> chatCompletion(
        @RequestBody @Valid ChatCompletionRequest chatCompletionRequest) {
        if (Boolean.TRUE.equals(chatCompletionRequest.stream())) {
            return Mono.just(ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_EVENT_STREAM_VALUE)
                .body(Flux.concat(
                        chatService.chatCompletionStream(chatCompletionRequest)
                            .map(it -> ServerSentEvent.builder(it).build()),
                        Flux.just(ServerSentEvent.builder("[DONE]").build())
                    ).contextWrite(context -> context.put("model", chatCompletionRequest.model()))
                ));
        } else {
            return chatService.chatCompletion(chatCompletionRequest)
                .map(it -> ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(it)
                ).contextWrite(context -> context.put("model", chatCompletionRequest.model()))
                .map(it -> it);
        }
    }
}
