package chat.gptalk.gateway.security;

import chat.gptalk.security.model.OpenApiUser;
import java.util.Map;
import java.util.UUID;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class InternalAuthenticationToken extends AbstractAuthenticationToken {

    private String jwtToken;
    private UUID apiKeyId;
    private OpenApiUser user;

    public InternalAuthenticationToken(String jwtToken, String apiKeyId) {
        super(null);
        setAuthenticated(false);
        this.jwtToken = jwtToken;
        this.apiKeyId = UUID.fromString(apiKeyId);
    }

    public InternalAuthenticationToken(OpenApiUser user) {
        super(null);
        setAuthenticated(true);
        this.user = user;
    }

    @Override
    public Object getCredentials() {
        return Map.of("jwtToken", jwtToken, "apiKeyId", apiKeyId);
    }

    @Override
    public Object getPrincipal() {
        return user;
    }
}
