package chat.gptalk.connector.sp.openai;

import chat.gptalk.common.util.JsonUtils;
import chat.gptalk.connector.sp.model.chat.ChatCompletion;
import chat.gptalk.connector.sp.model.chat.ChatCompletionChunk;
import chat.gptalk.connector.sp.model.chat.ChatCompletionRequest;
import chat.gptalk.connector.sp.model.chat.MediaContent;
import chat.gptalk.connector.sp.model.embedding.Embedding;
import chat.gptalk.connector.sp.model.embedding.EmbeddingList;
import chat.gptalk.connector.sp.model.embedding.EmbeddingRequest;
import java.util.List;
import java.util.function.Predicate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class OpenAiApi {

    public static final String DEFAULT_BASE_URL = "https://api.openai.com";

    public static final String DEFAULT_EMBEDDING_MODEL = "text-embedding-ada-002";

    private static final Predicate<String> SSE_DONE_PREDICATE = "[DONE]"::equals;

    private final String completionsPath;

    private final String embeddingsPath;

    private final WebClient webClient;

    public OpenAiApi(String baseUrl, WebClient webClient) {
        this.completionsPath = "/v1/chat/completions";
        this.embeddingsPath = "/v1/embeddings";
        this.webClient = webClient
            .mutate()
            .baseUrl(baseUrl)
            .build();
    }

    public static String getTextContent(List<MediaContent> content) {
        return content.stream()
            .filter(c -> "text".equals(c.type()))
            .map(MediaContent::text)
            .reduce("", (a, b) -> a + b);
    }

    public Mono<ChatCompletion> chatCompletion(ChatCompletionRequest chatRequest) {

        return this.webClient.post()
            .uri(this.completionsPath)
            .body(BodyInserters.fromValue(chatRequest))
            .retrieve()
            .bodyToMono(ChatCompletion.class);
    }

    public Flux<ChatCompletionChunk> chatCompletionStream(
        ChatCompletionRequest chatRequest) {

        Assert.notNull(chatRequest, "The request body can not be null.");
        Assert.isTrue(chatRequest.stream(), "Request must set the stream property to true.");

        return this.webClient.post()
            .uri(this.completionsPath)
            .body(Mono.just(chatRequest), ChatCompletionRequest.class)
            .retrieve()
            .bodyToFlux(String.class)
            // cancels the flux stream after the "[DONE]" is received.
            .takeUntil(SSE_DONE_PREDICATE)
            // filters out the "[DONE]" message.
            .filter(SSE_DONE_PREDICATE.negate())
            .map(content -> JsonUtils.toObject(content, ChatCompletionChunk.class));
    }

    public <T> Mono<EmbeddingList<Embedding>> embeddings(
        EmbeddingRequest<T> embeddingRequest) {

        Assert.notNull(embeddingRequest, "The request body can not be null.");

        // Input text to embed, encoded as a string or array of tokens. To embed multiple
        // inputs in a single
        // request, pass an array of strings or array of token arrays.
        Assert.notNull(embeddingRequest.input(), "The input can not be null.");
        Assert.isTrue(embeddingRequest.input() instanceof String || embeddingRequest.input() instanceof List,
            "The input must be either a String, or a List of Strings or List of List of integers.");

        // The input must not exceed the max input tokens for the model (8192 tokens for
        // text-embedding-ada-002), cannot
        // be an empty string, and any array must be 2048 dimensions or less.
        if (embeddingRequest.input() instanceof List list) {
            Assert.isTrue(!CollectionUtils.isEmpty(list), "The input list can not be empty.");
            Assert.isTrue(list.size() <= 2048, "The list must be 2048 dimensions or less");
            Assert.isTrue(
                list.get(0) instanceof String || list.get(0) instanceof Integer || list.get(0) instanceof List,
                "The input must be either a String, or a List of Strings or list of list of integers.");
        }

        return this.webClient.post()
            .uri(this.embeddingsPath)
            .body(BodyInserters.fromValue(embeddingRequest))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<>() {
            });
    }
}

