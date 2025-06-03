package chat.gptalk.connector.service;

import chat.gptalk.connector.sp.ModelClientFactory;
import chat.gptalk.connector.sp.model.chat.ChatCompletion;
import chat.gptalk.connector.sp.model.chat.ChatCompletionChunk;
import chat.gptalk.connector.sp.model.chat.ChatCompletionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ModelClientFactory clientFactory;

    public Mono<ChatCompletion> chatCompletion(ChatCompletionRequest chatCompletionRequest) {
        return Mono.deferContextual(contextView -> {
            return clientFactory.getChatClient(chatCompletionRequest.model())
                .flatMap(client -> client.chatCompletion(chatCompletionRequest));
        });
    }

    public Flux<ChatCompletionChunk> chatCompletionStream(ChatCompletionRequest chatCompletionRequest) {
        return clientFactory.getChatClient(chatCompletionRequest.model())
            .flatMapMany(client -> client.chatCompletionStream(chatCompletionRequest));
    }
}
