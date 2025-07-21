package chat.gptalk.admin.security;

import chat.gptalk.security.JwksManager;
import chat.gptalk.security.model.AdminUser;
import chat.gptalk.security.util.JwtVerifyUtils;
import java.security.interfaces.RSAPublicKey;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtVerifyUtils jwtVerifyUtils;

    public JwtAuthenticationProvider(JwksManager jwksManager) {
        RSAPublicKey publicKey = jwksManager.loadJwksPublicKey();
        this.jwtVerifyUtils = new JwtVerifyUtils(publicKey);
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        try {
            AdminUser user = jwtVerifyUtils.verifyAndParse(jwtAuthenticationToken.getAccessToken());
            return new JwtAuthenticationToken(user);
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid JWT Token", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}