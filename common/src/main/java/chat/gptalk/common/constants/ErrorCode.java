package chat.gptalk.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // ========== Common ==========
    CM_INVALID_PARAM("CM400001", HttpStatus.BAD_REQUEST, "Invalid parameter: {0}"),
    CM_MISSING_PARAM("CM400002", HttpStatus.BAD_REQUEST, "Missing required parameter: {0}"),
    CM_NOT_FOUND("CM404001", HttpStatus.NOT_FOUND, "{0}[{1}] not found"),
    CM_SYSTEM_ERROR("CM500001", HttpStatus.INTERNAL_SERVER_ERROR, "System error occurred"),

    // ========== Auth ==========
    AU_INVALID_TOKEN("AU401001", HttpStatus.UNAUTHORIZED, "Invalid token"),
    AU_INVALID_CREDENTIALS("AU401002", HttpStatus.UNAUTHORIZED, "Invalid username/password"),
    AU_UNAUTHORIZED("AU401001", HttpStatus.UNAUTHORIZED, "Unauthorized"),
    AU_FORBIDDEN("AU401002", HttpStatus.FORBIDDEN, "Forbidden"),
    AU_DUPLICATE_USER("AU044090", HttpStatus.CONFLICT, "Username[{0}] already exists"),

    // ========== Console ==========
    ADM_PROVIDER_ASSOCIATED_MODEL("ADM044001", HttpStatus.BAD_REQUEST,
        "This provider is associated with one or more models. Please remove those models before deleting the provider."),
    ADM_PROVIDER_ASSOCIATED_KEY("ADM044002", HttpStatus.BAD_REQUEST,
        "This provider has associated keys. Please delete the keys before proceeding."),
    ADM_CONFLICT("ADM044090", HttpStatus.CONFLICT, "{0} already exists"),

    // ========== Gateway ==========
    GW_TOO_MANY_REQUESTS("GW070001", HttpStatus.TOO_MANY_REQUESTS, "Rate limit exceeded"),

    // ========== Connector ==========
    CN_UNSUPPORTED_MODEL("CN400001", HttpStatus.BAD_REQUEST, "Unsupported model: {0}"),
    CN_VMODEL_NOT_BIND("CN400010", HttpStatus.BAD_REQUEST, "Virtual model: {0} not bind any models"),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}