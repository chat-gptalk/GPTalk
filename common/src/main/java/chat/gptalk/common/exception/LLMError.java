package chat.gptalk.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;

@JsonInclude(Include.NON_NULL)
public record LLMError(ErrorDetails error) {

    @Builder
    @JsonInclude(Include.NON_NULL)
    public record ErrorDetails(
        String code,
        String message,
        String param,   // Optional field
        String type     // Optional field
    ) {

    }
}
