package chat.gptalk.connector.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebConfig {


    @Bean
    public WebClient llmWebClient() {
        return WebClient.builder()
            .build();
    }
}
