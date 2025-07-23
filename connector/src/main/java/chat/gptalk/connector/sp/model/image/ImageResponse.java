package chat.gptalk.connector.sp.model.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ImageResponse(
    @JsonProperty("created") Long created,
    @JsonProperty("data") List<Data> data) {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Data(
        @JsonProperty("url") String url, @JsonProperty("b64_json") String b64Json,
        @JsonProperty("revised_prompt") String revisedPrompt) {

    }
}
