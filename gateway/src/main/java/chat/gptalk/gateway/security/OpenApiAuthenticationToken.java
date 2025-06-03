package chat.gptalk.gateway.security;

import chat.gptalk.common.security.ApiAuthenticatedUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class OpenApiAuthenticationToken extends AbstractAuthenticationToken {

    private String token;
    private ApiAuthenticatedUser user;

    public OpenApiAuthenticationToken(String token) {
        super(null);
        setAuthenticated(false);
        this.token = token;
    }

    public OpenApiAuthenticationToken(ApiAuthenticatedUser user) {
        super(null);
        setAuthenticated(true);
        this.user = user;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }
}
