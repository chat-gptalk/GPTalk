package chat.gptalk.auth.util;

import chat.gptalk.common.exception.UnauthorizedException;
import chat.gptalk.common.security.ConsoleAuthenticatedUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    public static ConsoleAuthenticatedUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        if(authentication.isAuthenticated()) {
            return (ConsoleAuthenticatedUser)authentication.getPrincipal();
        }
        throw new UnauthorizedException();
    }
}
