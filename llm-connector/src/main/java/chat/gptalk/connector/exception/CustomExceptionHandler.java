package chat.gptalk.connector.exception;

import chat.gptalk.common.exception.ApiException;
import chat.gptalk.common.exception.CommonErrorCode;
import chat.gptalk.common.util.JsonUtils;
import chat.gptalk.common.util.MessageUtils;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler {


    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<LLMError>> handleWebExchangeBindException(WebExchangeBindException ex) {
        String message = ex.getFieldErrors().stream().map(it -> it.getField() + ": " + it.getDefaultMessage())
            .collect(Collectors.joining(", "));
        log.info(message);
        return Mono.just(ResponseEntity
            .status(ex.getStatusCode())
            .body(new LLMError(LLMError.ErrorDetails.builder()
                .code(CommonErrorCode.BAD_REQUEST.errorCode())
                .message(message)
                .build()))
        );
    }

    @ExceptionHandler(WebClientResponseException.class)
    public Mono<ResponseEntity<LLMError>> handleWebExchangeBindException(WebClientResponseException ex) {
        String message = ex.getMessage();
        log.info(message, ex);
        String error = ex.getResponseBodyAsString();
        LLMError LLMError = JsonUtils.toObject(error, LLMError.class);
        return Mono.just(ResponseEntity
            .status(ex.getStatusCode())
            .body(LLMError)
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public Mono<ResponseEntity<LLMError>> handleResponseStatusException(ResponseStatusException ex) {
        String message = ex.getReason();
        if (ex.getCause() instanceof DecodingException je) {
            message = je.getMessage();
        }
        log.info(message);
        return Mono.just(ResponseEntity
            .status(ex.getStatusCode())
            .body(new LLMError(LLMError.ErrorDetails.builder()
                .code(CommonErrorCode.BAD_REQUEST.errorCode())
                .message(message)
                .build())));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Mono<ResponseEntity<LLMError>> handleAccessDeniedException(AccessDeniedException ex) {
        log.info(ex.getMessage());
        return Mono.just(ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(new LLMError(LLMError.ErrorDetails.builder()
                .code(CommonErrorCode.BAD_REQUEST.errorCode())
                .message(ex.getMessage())
                .build())));
    }

    @ExceptionHandler(LLMException.class)
    public Mono<ResponseEntity<LLMError>> handleLLMException(LLMException ex) {
        log.error(ex.getError().error().code() + ":" + ex.getMessage());
        return Mono.just(ResponseEntity
            .status(ex.getStatus())
            .body(ex.getError()));
    }

    @ExceptionHandler(ApiException.class)
    public Mono<ResponseEntity<LLMError>> handleApiException(ApiException ex) {
        String message = MessageUtils.getMessage(ex.getDetail());
        log.info(message);
        return Mono.just(ResponseEntity
            .status(ex.getStatus())
            .body(new LLMError(LLMError.ErrorDetails.builder()
                .code(CommonErrorCode.BAD_REQUEST.errorCode())
                .message(message)
                .build())));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<LLMError>> handleGeneralException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return Mono.just(ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new LLMError(LLMError.ErrorDetails.builder()
                .code(CommonErrorCode.BAD_REQUEST.errorCode())
                .message(MessageUtils.getMessage(CommonErrorCode.INTERNAL_SERVER_ERROR.errorCode()))
                .build())));
    }
}
