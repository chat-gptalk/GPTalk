package chat.gptalk.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("gptalk.console")
public record ConsoleProperties(Gateway gateway) {

    public record Gateway(String baseUrl) {}
}
