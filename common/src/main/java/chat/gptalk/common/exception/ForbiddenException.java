package chat.gptalk.common.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends ApiException{

    public ForbiddenException() {
        super(HttpStatus.FORBIDDEN, CommonErrorCode.FORBIDDEN);
    }
}
