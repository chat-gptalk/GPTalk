package chat.gptalk.auth.security;

import chat.gptalk.auth.config.AuthProperties;
import chat.gptalk.auth.util.JwtUtils;
import chat.gptalk.auth.util.RequestUtils;
import chat.gptalk.common.security.ConsoleAuthenticatedUser;
import chat.gptalk.common.security.SecurityConstants;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtUtils jwtUtils;

    public JwtAuthenticationProvider(AuthProperties authProperties) {
        this.jwtUtils = new JwtUtils(authProperties.jwt().publicKey(), authProperties.jwt().privateKey());
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        try {
            ConsoleAuthenticatedUser user = jwtUtils.verifyAndParse(jwtAuthenticationToken.getAccessToken());
            return new JwtAuthenticationToken(user);
        } catch (JWTVerificationException e) {
            if (!StringUtils.hasText(jwtAuthenticationToken.getRefreshToken())) {
                return null;
            }
            ConsoleAuthenticatedUser user = jwtUtils.verifyAndParse(jwtAuthenticationToken.getRefreshToken());
            String newAccessToken = jwtUtils.generateToken(user, SecurityConstants.ACCESS_TOKEN_DURATION);
            Cookie cookie = new Cookie(SecurityConstants.ACCESS_TOKEN, newAccessToken);
            cookie.setPath("/");
            cookie.setSecure(true);
            cookie.setMaxAge((int) SecurityConstants.ACCESS_TOKEN_DURATION.getSeconds());
            cookie.setHttpOnly(true);
            cookie.setAttribute("sameSite", "Strict");
            HttpServletResponse response = RequestUtils.getResponse();
            if (response != null) {
                response.addCookie(cookie);
            }
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