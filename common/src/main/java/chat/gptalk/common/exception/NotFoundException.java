package chat.gptalk.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, CommonErrorCode.NOT_FOUND, new ErrorDetail(message));
    }
}
