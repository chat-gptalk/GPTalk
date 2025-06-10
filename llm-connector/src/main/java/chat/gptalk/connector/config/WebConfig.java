package chat.gptalk.connector.config;

import chat.gptalk.connector.security.ApiKeyInjectionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebConfig {

    private final ApiKeyInjectionFilter apiKeyInjectionFilter;

    @Bean
    public WebClient llmWebClient() {
        return WebClient.builder()
           // .filter(apiKeyInjectionFilter)
            .build();
    }
}
