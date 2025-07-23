package chat.gptalk.connector.sp.model.embedding;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public record Embedding(
    @JsonProperty("index") Integer index,
    @JsonProperty("embedding") float[] embedding,
    @JsonProperty("object") String object) {
}
