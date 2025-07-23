package chat.gptalk.connector.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gptalk.connector")
public record ConnectorProperties(LlmProvider llmProvider) {

    public record LlmProvider(
        String baseUrl
    ) {

    }

}
