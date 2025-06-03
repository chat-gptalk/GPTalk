package chat.gptalk.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ApiException extends RuntimeException {

    private final HttpStatus status;
    private final ErrorCode errorCode;
    private final ErrorDetail detail;

    public record ErrorDetail(
        String messageKey,
        Object... messageArgs
    ) {

    }

    public ApiException(HttpStatus status, ErrorCode errorCode, ErrorDetail detail) {
        super(detail.messageKey);
        this.status = status;
        this.errorCode = errorCode;
        this.detail = detail;
    }

    public ApiException(HttpStatus status, ErrorCode errorCode) {
        super(errorCode.errorCode());
        this.status = status;
        this.errorCode = errorCode;
        this.detail = new ErrorDetail(errorCode.errorCode());
    }
}
