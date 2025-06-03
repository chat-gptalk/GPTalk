package chat.gptalk.connector.sp.openai;

import chat.gptalk.connector.sp.ChatClient;
import chat.gptalk.connector.sp.model.chat.ChatCompletion;
import chat.gptalk.connector.sp.model.chat.ChatCompletionChunk;
import chat.gptalk.connector.sp.model.chat.ChatCompletionRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class OpenAiChatClient implements OpenAiClient, ChatClient {

    private final OpenAiApi openAiApi;

    public OpenAiChatClient(WebClient llmWebClient) {
        String baseUrl = System.getenv("OPENAI_BASE_URL");
        this.openAiApi = new OpenAiApi(baseUrl, llmWebClient);
    }

    @Override
    public Mono<Boolean> support(String model) {
        return Mono.just(true);
    }

    @Override
    public Mono<ChatCompletion> chatCompletion(ChatCompletionRequest request) {
        return openAiApi.chatCompletion(request);
    }

    @Override
    public Flux<ChatCompletionChunk> chatCompletionStream(ChatCompletionRequest request) {
        return openAiApi.chatCompletionStream(request);
    }
}
