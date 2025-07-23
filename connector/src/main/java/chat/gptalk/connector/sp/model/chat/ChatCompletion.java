package chat.gptalk.connector.sp.model.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public record ChatCompletion(

    @JsonProperty("id")
    String id,
    @JsonProperty("choices")
    List<Choice> choices,
    @JsonProperty("created")
    Long created,
    @JsonProperty("model")
    String model,
    @JsonProperty("service_tier")
    String serviceTier,
    @JsonProperty("system_fingerprint")
    String systemFingerprint,
    @JsonProperty("object")
    String object,
    @JsonProperty("usage")
    Usage usage
) {

    @JsonInclude(Include.NON_NULL)
    public record Choice(
        @JsonProperty("finish_reason") ChatCompletionFinishReason finishReason,
        @JsonProperty("index") Integer index,
        @JsonProperty("message") ChatCompletionMessage message,
        @JsonProperty("logprobs") LogProbs logprobs) {
    }

}
