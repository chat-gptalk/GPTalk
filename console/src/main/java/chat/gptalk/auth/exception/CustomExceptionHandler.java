package chat.gptalk.auth.exception;

import static chat.gptalk.common.constants.GPTalkConstants.ERROR_URL;

import chat.gptalk.common.exception.ApiException;
import chat.gptalk.common.exception.CommonErrorCode;
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

        ProblemDetail problemDetail = ProblemDetail.forStatus(CommonErrorCode.BAD_REQUEST.httpStatus());
        problemDetail.setTitle("Bad Request");
        problemDetail.setDetail(message);
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setType(URI.create(ERROR_URL));
        problemDetail.setProperty("error_code", CommonErrorCode.BAD_REQUEST.errorCode());

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

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        String code = HttpStatus.FORBIDDEN.name();
        problemDetail.setTitle(code);
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setType(URI.create(ERROR_URL));
        problemDetail.setProperty("error_code", CommonErrorCode.FORBIDDEN.errorCode());
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(problemDetail);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ProblemDetail> handleApiException(HttpServletRequest request, ApiException ex) {
        String message = MessageUtils.getMessage(ex.getDetail());
        log.info(message);
        ProblemDetail problemDetail = ProblemDetail.forStatus(ex.getStatus());
        problemDetail.setDetail(message);
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setType(URI.create(ERROR_URL));
        problemDetail.setProperty("category", ex.getErrorCode().category());
        problemDetail.setProperty("identifier", ex.getErrorCode().identifier());
        return ResponseEntity
            .status(ex.getStatus())
            .body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneralException(HttpServletRequest request, Exception ex) {
        log.error(ex.getMessage(), ex);
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setType(URI.create(ERROR_URL));
        problemDetail.setProperty("error_code", CommonErrorCode.INTERNAL_SERVER_ERROR.errorCode());

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(problemDetail);
    }
}
