package chat.gptalk.security.util;

import chat.gptalk.common.constants.ErrorCode;
import chat.gptalk.common.exception.BizException;
import chat.gptalk.security.model.AdminUser;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    public static AdminUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        if(authentication.isAuthenticated()) {
            return (AdminUser)authentication.getPrincipal();
        }
        throw new BizException(ErrorCode.AU_UNAUTHORIZED);
    }

    public static UUID getTenantId() {
        return getCurrentUser().tenantId();
    }
}
