package chat.gptalk.security.security;

import chat.gptalk.common.security.ApiAuthenticatedUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class GatewayAuthenticationToken extends AbstractAuthenticationToken {

    private final ApiAuthenticatedUser user;

    public GatewayAuthenticationToken(ApiAuthenticatedUser user) {
        super(null);
        setAuthenticated(true);
        this.user = user;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }
}
