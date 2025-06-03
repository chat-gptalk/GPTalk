package chat.gptalk.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("gptalk.auth")
public record AuthProperties(Jwt jwt) {

    public record Jwt (String publicKey, String privateKey) {}
}
