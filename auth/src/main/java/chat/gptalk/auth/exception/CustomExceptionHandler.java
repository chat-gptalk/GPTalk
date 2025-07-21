package chat.gptalk.auth.exception;

import static chat.gptalk.common.constants.GPTalkConstants.ERROR_URL;

import chat.gptalk.common.constants.ErrorCode;
import chat.gptalk.common.exception.BizException;
import chat.gptalk.common.util.MessageUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ProblemDetail> handleBindException(HttpServletRequest request, BindException ex) {
        String message = ex.getFieldErrors().stream().map(it -> it.getField() + ": " + it.getDefaultMessage())
            .collect(Collectors.joining(", "));
        log.info(message);

        ErrorCode errorCode = ErrorCode.CM_INVALID_PARAM;
        ProblemDetail problemDetail = ProblemDetail.forStatus(errorCode.getHttpStatus());
        problemDetail.setTitle("Bad Request");
        problemDetail.setDetail(message);
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setType(URI.create(ERROR_URL));
        problemDetail.setProperty("error_code", errorCode.getCode());

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(problemDetail);
    }


    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ProblemDetail> handleResponseStatusException(HttpServletRequest request,
        ResponseStatusException ex) {
        String message = ex.getReason();
        if (ex.getCause() instanceof DecodingException je) {
            message = je.getMessage();
        }
        log.info(message);

        ProblemDetail problemDetail = ProblemDetail.forStatus(ex.getStatusCode());
        String code = ((HttpStatus) ex.getStatusCode()).name();
        problemDetail.setTitle(code);
        problemDetail.setDetail(message);
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setType(URI.create(ERROR_URL));
        problemDetail.setProperty("error_code", code);

        return ResponseEntity
            .status(ex.getStatusCode())
            .body(problemDetail);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDeniedException(HttpServletRequest request,
        AccessDeniedException ex) {
        log.info(ex.getMessage());

        ErrorCode errorCode = ErrorCode.AU_FORBIDDEN;
        ProblemDetail problemDetail = ProblemDetail.forStatus(errorCode.getHttpStatus());
        problemDetail.setTitle(errorCode.name());
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setType(URI.create(ERROR_URL));
        problemDetail.setProperty("error_code", errorCode.getCode());
        return ResponseEntity
            .status(errorCode.getHttpStatus())
            .body(problemDetail);
    }

    @ExceptionHandler(BizException.class)
    public ResponseEntity<ProblemDetail> handleApiException(HttpServletRequest request, BizException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        String message = MessageUtils.resolveMessage(request.getLocale(), errorCode.name(), ex.getArgs());
        log.info(message);
        ProblemDetail problemDetail = ProblemDetail.forStatus(errorCode.getHttpStatus());
        problemDetail.setDetail(message);
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setType(URI.create(ERROR_URL));
        return ResponseEntity
            .status(errorCode.getHttpStatus())
            .body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneralException(HttpServletRequest request, Exception ex) {
        log.error(ex.getMessage(), ex);
        ErrorCode errorCode = ErrorCode.CM_SYSTEM_ERROR;
        ProblemDetail problemDetail = ProblemDetail.forStatus(errorCode.getHttpStatus());
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setType(URI.create(ERROR_URL));
        problemDetail.setProperty("error_code", errorCode.getCode());

        return ResponseEntity
            .status(errorCode.getHttpStatus())
            .body(problemDetail);
    }
}
