package chat.gptalk.connector.sp;

import chat.gptalk.connector.sp.model.chat.ChatCompletion;
import chat.gptalk.connector.sp.model.chat.ChatCompletionChunk;
import chat.gptalk.connector.sp.model.chat.ChatCompletionRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ChatClient extends LLMClient {

    Mono<Boolean> support(String model);

    Mono<ChatCompletion> chatCompletion(ChatCompletionRequest request);

    Flux<ChatCompletionChunk> chatCompletionStream(ChatCompletionRequest request);
}
