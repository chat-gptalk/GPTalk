package chat.gptalk.auth.util;

import chat.gptalk.common.security.ConsoleAuthenticatedUser;
import chat.gptalk.common.security.SecurityConstants;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

public final class JwtUtils {

    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public JwtUtils(String publicKey, String privateKey) {
        this.algorithm = Algorithm.RSA256(parsePublicKey(publicKey), parsePrivateKey(privateKey));
        this.verifier = JWT.require(algorithm).build();
    }

    public String generateToken(ConsoleAuthenticatedUser user, Duration expiration) {
        return JWT.create()
            .withIssuer("gptalk-auth")
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plus(expiration))
            .withSubject(user.userId().toString())
            .withClaim(SecurityConstants.CLAIM_TENANT_ID, user.tenantId().toString())
            .withClaim(SecurityConstants.CLAIM_USERNAME, user.username())
            .withClaim(SecurityConstants.CLAIM_ROLES, user.roles())
            .sign(algorithm);
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

    public static String toBase64Url(BigInteger value) {
        byte[] bytes = value.toByteArray();
        if (bytes[0] == 0) {
            // remove leading zero byte
            byte[] tmp = new byte[bytes.length - 1];
            System.arraycopy(bytes, 1, tmp, 0, tmp.length);
            bytes = tmp;
        }
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static RSAPrivateKey parsePrivateKey(String pem) {
        try {
            String privateKeyPEM = pem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

            byte[] keyBytes = Base64.getDecoder().decode(privateKeyPEM);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(keySpec);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid RSA Public Key", e);
        }
    }

    public static RSAPublicKey parsePublicKey(String pem) {
        try {
            String clean = pem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

            byte[] decoded = Base64.getDecoder().decode(clean);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid RSA Public Key", e);
        }
    }
}
