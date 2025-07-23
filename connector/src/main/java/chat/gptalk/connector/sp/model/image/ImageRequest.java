package chat.gptalk.connector.sp.model.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ImageRequest(
    @JsonProperty("prompt") String prompt,
    @JsonProperty("model") String model,
    @JsonProperty("n") Integer n,
    @JsonProperty("quality") String quality,
    @JsonProperty("response_format") String responseFormat,
    @JsonProperty("size") String size,
    @JsonProperty("style") String style,
    @JsonProperty("user") String user) {

}
