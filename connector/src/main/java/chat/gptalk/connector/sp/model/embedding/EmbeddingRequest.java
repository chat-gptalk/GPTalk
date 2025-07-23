package chat.gptalk.connector.sp.model.embedding;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public record EmbeddingRequest<T>(
    @JsonProperty("input") T input,
    @JsonProperty("model") String model,
    @JsonProperty("encoding_format") String encodingFormat,
    @JsonProperty("dimensions") Integer dimensions,
    @JsonProperty("user") String user) {
}
