package chat.gptalk.common.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ApiException {

    public BadRequestException(String messageKey) {
        super(HttpStatus.BAD_REQUEST, CommonErrorCode.BAD_REQUEST,
            new ErrorDetail(messageKey));
    }

    public BadRequestException(ErrorDetail detail) {
        super(HttpStatus.BAD_REQUEST, CommonErrorCode.BAD_REQUEST, detail);
    }
}
