package chat.gptalk.connector.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class OpenApiAuthenticationToken extends AbstractAuthenticationToken {

    private String token;
    private OpenApiUser user;

    public OpenApiAuthenticationToken(String token) {
        super(null);
        setAuthenticated(false);
        this.token = token;
    }

    public OpenApiAuthenticationToken(OpenApiUser user) {
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
