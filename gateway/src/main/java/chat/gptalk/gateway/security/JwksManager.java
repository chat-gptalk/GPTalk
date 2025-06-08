package chat.gptalk.gateway.security;

import chat.gptalk.gateway.config.GatewayProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class JwksManager {

    @Getter
    private RSAPublicKey publicKey;
    private final WebClient webClient;
    private final GatewayProperties gatewayProperties;

    @PostConstruct
    public void loadJwks() throws Exception {
        String jwksUrl = gatewayProperties.auth().baseUrl() + "/.well-known/jwks.json";
        String json = webClient.get().uri(jwksUrl).retrieve().bodyToMono(String.class)
            .block();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node;
        try {
            node = mapper.readTree(json).get("keys").get(0);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String n = node.get("n").asText();
        String e = node.get("e").asText();
        publicKey = constructRsaPublicKey(n, e);
    }

    private RSAPublicKey constructRsaPublicKey(String n, String e) {
        BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(n));
        BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(e));

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) keyFactory.generatePublic(new RSAPublicKeySpec(modulus, exponent));
        } catch (Exception ex) {
            throw new RuntimeException("Failed to construct public key", ex);
        }
    }
}
