package chat.gptalk.gateway.util;

import chat.gptalk.common.security.ConsoleAuthenticatedUser;
import chat.gptalk.common.security.SecurityConstants;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

public final class JwtUtils {

    private final JWTVerifier verifier;

    public JwtUtils(RSAPublicKey publicKey) {
        Algorithm algorithm = Algorithm.RSA256(publicKey);
        this.verifier = JWT.require(algorithm).build();
    }

    public ConsoleAuthenticatedUser verifyAndParse(String token) {
        DecodedJWT decodedJWT = verify(token);
        return new ConsoleAuthenticatedUser(
            UUID.fromString(decodedJWT.getSubject()),
            decodedJWT.getClaim(SecurityConstants.CLAIM_USERNAME).asString(),
            UUID.fromString(decodedJWT.getClaim(SecurityConstants.CLAIM_TENANT_ID).asString()),
            decodedJWT.getClaim(SecurityConstants.CLAIM_ROLES).asList(String.class)
        );
    }

    private DecodedJWT verify(String token) throws JWTVerificationException {
        return verifier.verify(token);
    }

}