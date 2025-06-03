package chat.gptalk.common.security;

import java.time.Duration;

public final class SecurityConstants {

    private SecurityConstants() {
    }

    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";

    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofMinutes(30);
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(7);


    public static final String HEADER_USER_ID = "X-User-Id";
    public static final String HEADER_USERNAME = "X-Username-Base64";
    public static final String HEADER_ROLES = "X-Roles";
    public static final String HEADER_TENANT_ID = "X-Tenant-Id";
    public static final String HEADER_API_KEY_ID = "X-API-KEY-Id";
    public static final String HEADER_CLIENT_ID = "X-Client-Id";

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    public static final String CLAIM_USER_ID = "user_id";
    public static final String CLAIM_USERNAME = "username";
    public static final String CLAIM_ROLES = "roles";
    public static final String CLAIM_TENANT_ID = "tenant_id";
    public static final String CLAIM_CLIENT_ID = "client_id";

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String JWKS_URI = "/.well-known/jwks.json";

    public static final String TOKEN_TYPE_ACCESS = "access_token";
    public static final String TOKEN_TYPE_REFRESH = "refresh_token";
}
