package chat.gptalk.common.exception;

public interface ErrorCode {

    Integer httpStatus();

    String category();

    String identifier();

    default String errorCode() {
        return String.format("%s_%s", category(), identifier());
    }
}
