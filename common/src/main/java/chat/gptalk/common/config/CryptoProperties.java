package chat.gptalk.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gptalk.crypto")
public record CryptoProperties(String type, Aes aes) {

    public record Aes(
        String secret
    ) {

    }

}
