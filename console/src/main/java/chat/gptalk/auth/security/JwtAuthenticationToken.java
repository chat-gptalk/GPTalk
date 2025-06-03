package chat.gptalk.auth.security;

import chat.gptalk.common.security.ConsoleAuthenticatedUser;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    @Getter
    private String accessToken;
    @Getter
    private String refreshToken;
    private ConsoleAuthenticatedUser user;

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

    public JwtAuthenticationToken(ConsoleAuthenticatedUser user) {
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
