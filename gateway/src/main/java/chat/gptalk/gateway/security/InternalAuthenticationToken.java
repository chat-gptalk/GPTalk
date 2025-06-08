package chat.gptalk.gateway.security;

import chat.gptalk.common.security.ApiAuthenticatedUser;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class InternalAuthenticationToken extends AbstractAuthenticationToken {

    private String token;
    @Getter
    private String apiKeyId;
    private ApiAuthenticatedUser user;

    public InternalAuthenticationToken(String token, String apiKeyId) {
        super(null);
        setAuthenticated(false);
        this.token = token;
        this.apiKeyId = apiKeyId;
    }

    public InternalAuthenticationToken(ApiAuthenticatedUser user) {
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
