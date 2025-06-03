package chat.gptalk.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gptalk.gateway")
public record GatewayProperties(
    Auth auth,
    Security security
) {

    public record Auth(
        String baseUrl
    ) {

    }

    public record Security(
        Jwt jwt
    ) {

    }

    public record Jwt(
        String publicKey
    ) {

    }
}
