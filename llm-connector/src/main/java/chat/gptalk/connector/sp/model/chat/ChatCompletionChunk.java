package chat.gptalk.connector.sp.model.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public record ChatCompletionChunk(
    @JsonProperty("id") String id,
    @JsonProperty("choices") List<ChunkChoice> choices,
    @JsonProperty("created") Long created,
    @JsonProperty("model") String model,
    @JsonProperty("service_tier") String serviceTier,
    @JsonProperty("system_fingerprint") String systemFingerprint,
    @JsonProperty("object") String object,
    @JsonProperty("usage") Usage usage) {

    @JsonInclude(Include.NON_NULL)
    public record ChunkChoice(
        @JsonProperty("finish_reason") ChatCompletionFinishReason finishReason,
        @JsonProperty("index") Integer index,
        @JsonProperty("delta") ChatCompletionMessage delta,
        @JsonProperty("logprobs") LogProbs logprobs) {

    }
}
