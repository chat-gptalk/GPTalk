package chat.gptalk.connector.service;

import chat.gptalk.common.exception.BadRequestException;
import chat.gptalk.connector.router.ModelRouter;
import chat.gptalk.connector.sp.ModelClientFactory;
import chat.gptalk.connector.sp.model.chat.ChatCompletion;
import chat.gptalk.connector.sp.model.chat.ChatCompletionChunk;
import chat.gptalk.connector.sp.model.chat.ChatCompletionRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ModelRouter modelRouter;
    private final ModelClientFactory clientFactory;

    public Mono<ChatCompletion> chatCompletion(UUID tenantId, ChatCompletionRequest chatCompletionRequest) {
        return modelRouter.resolve(tenantId, chatCompletionRequest.model())
            .flatMap(modelConfig -> clientFactory.getChatClient(modelConfig.provider().sdkClass())
                .flatMap(
                    client -> client.chatCompletion(modelConfig.provider().baseUrl(), modelConfig.providerKey().key(),
                        chatCompletionRequest.withModel(modelConfig.providerModel().providerModelName()))
                )
            );
    }

    public Flux<ChatCompletionChunk> chatCompletionStream(UUID tenantId, ChatCompletionRequest chatCompletionRequest) {
        return modelRouter.resolve(tenantId, chatCompletionRequest.model())
            .switchIfEmpty(Mono.error(new BadRequestException("Unsupported model: "+chatCompletionRequest.model())))
            .flatMapMany(modelConfig -> clientFactory.getChatClient(modelConfig.provider().sdkClass())
                .flatMapMany(
                    client -> client.chatCompletionStream(modelConfig.provider().baseUrl(), modelConfig.providerKey().key(),
                        chatCompletionRequest.withModel(modelConfig.providerModel().providerModelName()))
                )
            );
    }
}
