package chat.gptalk.common.exception;

//
public enum CommonErrorCode implements ErrorCode {
    BAD_REQUEST(400, "1000"),

    UNAUTHORIZED(401, "1000"),
    TOKEN_EMPTY(401, "1001"),
    UNAUTHORIZED_INVALID_TOKEN_FORMAT(401, "1002"),

    FORBIDDEN(403, "1000"),

    NOT_FOUND(404, "1000"),

    /*    METHOD_NOT_ALLOWED(4050000, messageKey),

        NOT_ACCEPTABLE(4060000, messageKey),

        REQUEST_TIMEOUT(4080000, messageKey),
        */
    CONFLICT(409, "1000"),

    PAYLOAD_TOO_LARGE(413, "1000"),

  /*  URI_TOO_LONG(4140000, messageKey),


    UNSUPPORTED_MEDIA_TYPE(4150000, messageKey),*/

    TOO_MANY_REQUESTS(429, "1000"),

    INTERNAL_SERVER_ERROR(500, "1000"),

//    BAD_GATEWAY(5020000, messageKey),

    SERVICE_UNAVAILABLE(503, "1000"),

//    GATEWAY_TIMEOUT(5040000, messageKey),
    ;
    private final int httpStatus;
    private final String identifier;

    CommonErrorCode(int httpStatus, String identifier) {
        this.httpStatus = httpStatus;
        this.identifier = identifier;
    }

    @Override
    public Integer httpStatus() {
        return httpStatus;
    }

    @Override
    public String category() {
        return "common";
    }

    @Override
    public String identifier() {
        return identifier;
    }
}
