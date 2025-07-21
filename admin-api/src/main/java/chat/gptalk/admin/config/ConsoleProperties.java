package chat.gptalk.admin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("gptalk.console")
public record ConsoleProperties() {

}
