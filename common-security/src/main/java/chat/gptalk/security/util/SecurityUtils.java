package chat.gptalk.security.util;

import chat.gptalk.common.security.ApiAuthenticatedUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import reactor.core.publisher.Mono;

public class SecurityUtils {

    public static Mono<ApiAuthenticatedUser> getCurrentUser() {
        return getCurrentAuthentication()
            .map(it -> (ApiAuthenticatedUser) it.getPrincipal());
    }

    private static Mono<Authentication> getCurrentAuthentication() {
        return ReactiveSecurityContextHolder.getContext()
            .map(SecurityContext::getAuthentication);
    }
}

