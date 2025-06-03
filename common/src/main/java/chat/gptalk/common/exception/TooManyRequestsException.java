package chat.gptalk.common.exception;

import org.springframework.http.HttpStatus;

public class TooManyRequestsException extends ApiException {

    protected TooManyRequestsException() {
        super(HttpStatus.TOO_MANY_REQUESTS, CommonErrorCode.TOO_MANY_REQUESTS);
    }
}
