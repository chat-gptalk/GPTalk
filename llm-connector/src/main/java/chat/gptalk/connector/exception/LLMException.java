package chat.gptalk.connector.exception;

import chat.gptalk.common.exception.ApiException;
import chat.gptalk.common.exception.CommonErrorCode;
import chat.gptalk.common.exception.LLMError;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public class LLMException extends ApiException {
    @Getter
    private LLMError error;

    public LLMException(LLMError error) {
        super(HttpStatus.BAD_REQUEST, CommonErrorCode.BAD_REQUEST,
            new ErrorDetail(error.error().message(), error.error().param()));
        this.error = error;
    }
}
