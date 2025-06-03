package chat.gptalk.connector.sp.model.embedding;

import chat.gptalk.connector.sp.model.chat.Usage;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public record EmbeddingList<T>(
    @JsonProperty("object") String object,
    @JsonProperty("data") List<T> data,
    @JsonProperty("model") String model,
    @JsonProperty("usage") Usage usage) {

}