package chat.gptalk.security.util;

import chat.gptalk.common.constants.ErrorCode;
import chat.gptalk.common.exception.BizException;
import chat.gptalk.security.model.AuthenticatedUser;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    @SuppressWarnings("unchecked")
    public static <T extends AuthenticatedUser> T getCurrentUser(Class<T> clazz) {
        Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        if (authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (clazz.isInstance(principal)) {
                return (T) principal;
            }
        }
        throw new BizException(ErrorCode.AU_UNAUTHORIZED);
    }

    public static UUID getTenantId() {
        return getCurrentUser(AuthenticatedUser.class).tenantId();
    }
}
