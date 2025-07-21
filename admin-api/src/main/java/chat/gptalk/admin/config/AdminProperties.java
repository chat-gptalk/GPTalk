package chat.gptalk.admin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("gptalk.admin")
public record AdminProperties() {

}
