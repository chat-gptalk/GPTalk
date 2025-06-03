package chat.gptalk.common.exception;

import org.springframework.http.HttpStatus;

public class UsageLimitExceededException extends ApiException{

    public UsageLimitExceededException(ErrorDetail detail) {
        super(HttpStatus.TOO_MANY_REQUESTS, CommonErrorCode.TOO_MANY_REQUESTS, detail);
    }
}
