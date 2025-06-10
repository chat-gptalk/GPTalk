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
        this.openAiApi = new OpenAiApi(llmWebClient);
    }

    @Override
    public Mono<ChatCompletion> chatCompletion(String baseUrl, String key, ChatCompletionRequest request) {
        return openAiApi.chatCompletion(baseUrl, key, request);
    }

    @Override
    public Flux<ChatCompletionChunk> chatCompletionStream(String baseUrl, String key, ChatCompletionRequest request) {
        return openAiApi.chatCompletionStream(baseUrl, key, request);
    }
}
