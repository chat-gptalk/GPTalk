package chat.gptalk.connector.sp.model.moderation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ModerationResponse(
    @JsonProperty("id") String id,
    @JsonProperty("model") String model,
    @JsonProperty("results") ModerationResult[] results) {

}
