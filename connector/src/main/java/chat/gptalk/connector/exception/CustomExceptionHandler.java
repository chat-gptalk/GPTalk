package chat.gptalk.connector.exception;

import chat.gptalk.common.constants.ErrorCode;
import chat.gptalk.common.exception.BizException;
import chat.gptalk.common.exception.LLMError;
import chat.gptalk.common.util.JsonUtils;
import chat.gptalk.common.util.MessageUtils;
import java.util.Locale;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
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
                .code(ErrorCode.CM_INVALID_PARAM.name())
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
                .code(ex.getReason())
                .message(message)
                .build())));
    }


    @ExceptionHandler(LlmException.class)
    public Mono<ResponseEntity<LLMError>> handleLLMException(LlmException ex) {
        log.error(ex.getError().error().code() + ":" + ex.getMessage());
        return Mono.just(ResponseEntity
            .status(ex.getStatus())
            .body(ex.getError()));
    }

    @ExceptionHandler(BizException.class)
    public Mono<ResponseEntity<LLMError>> handleApiException(ServerWebExchange exchange, BizException ex) {
        Locale locale = exchange.getLocaleContext().getLocale();
        String message = MessageUtils.resolveMessage(locale, ex.getErrorCode());
        log.info(message);
        return Mono.just(ResponseEntity
            .status(ex.getErrorCode().getHttpStatus())
            .body(new LLMError(LLMError.ErrorDetails.builder()
                .code(ex.getErrorCode().name())
                .message(message)
                .build())));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<LLMError>> handleGeneralException(ServerWebExchange exchange, Exception ex) {
        log.error(ex.getMessage(), ex);
        ErrorCode errorCode = ErrorCode.CM_SYSTEM_ERROR;
        return Mono.just(ResponseEntity
            .status(errorCode.getHttpStatus())
            .body(new LLMError(LLMError.ErrorDetails.builder()
                .code(errorCode.getCode())
                .message(MessageUtils.resolveMessage(exchange.getLocaleContext().getLocale(), errorCode))
                .build())));
    }
}
