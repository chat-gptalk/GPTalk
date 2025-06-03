package chat.gptalk.auth.exception;

import chat.gptalk.common.exception.ErrorCode;

public enum AuthErrorCode implements ErrorCode {
    INVALID_CREDENTIAL(401, "1002"),
    ;

    private final int httpStatus;
    private final String identifier;

    AuthErrorCode(int httpStatus, String identifier) {
        this.httpStatus = httpStatus;
        this.identifier = identifier;
    }

    @Override
    public Integer httpStatus() {
        return httpStatus;
    }

    @Override
    public String category() {
        return "auth";
    }

    @Override
    public String identifier() {
        return identifier;
    }
}
