package chat.gptalk.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebConfig {

    private final ConsoleProperties consoleProperties;

    @Bean
    public WebClient gatewayClient() {
        return WebClient.builder()
            .baseUrl(consoleProperties.gateway().baseUrl())
            .build();
    }

    @Bean
    public RestClient restClient() {
        return RestClient.builder().build();
    }
}
