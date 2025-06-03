package chat.gptalk.common.exception;

import org.springframework.http.HttpStatus;

public class DataConflictException extends ApiException {

    public DataConflictException(String messageKey) {
        super(HttpStatus.CONFLICT, CommonErrorCode.CONFLICT,
            new ErrorDetail(messageKey));
    }

    public DataConflictException(ErrorDetail detail) {
        super(HttpStatus.CONFLICT, CommonErrorCode.CONFLICT, detail);
    }
}
