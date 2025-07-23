package chat.gptalk.admin.security;

import chat.gptalk.security.model.AuthUser;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    @Getter
    private String accessToken;
    @Getter
    private String refreshToken;
    private AuthUser user;

    public JwtAuthenticationToken(String accessToken) {
        super(null);
        setAuthenticated(false);
        this.accessToken = accessToken;
        this.refreshToken = null;
    }

    public JwtAuthenticationToken(String accessToken, String refreshToken) {
        super(null);
        setAuthenticated(false);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public JwtAuthenticationToken(AuthUser user) {
        super(user.roles().stream().map(SimpleGrantedAuthority::new).toList());
        setAuthenticated(true);
        this.user = user;
    }

    @Override
    public Object getCredentials() {
        return accessToken;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }
}
