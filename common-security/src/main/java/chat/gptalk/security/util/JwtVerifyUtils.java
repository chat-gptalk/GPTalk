package chat.gptalk.security.util;

import chat.gptalk.security.SecurityConstants;
import chat.gptalk.security.model.AuthUser;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

public final class JwtVerifyUtils {

    private final JWTVerifier verifier;

    public JwtVerifyUtils(RSAPublicKey publicKey) {
        Algorithm algorithm = Algorithm.RSA256(publicKey);
        this.verifier = JWT.require(algorithm).build();
    }

    public AuthUser verifyAndParse(String token) {
        DecodedJWT decodedJWT = verify(token);
        return new AuthUser(
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