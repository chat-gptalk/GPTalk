package chat.gptalk.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final ConsoleProperties consoleProperties;

    @Bean
    public WebClient gatewayClient() {
        return WebClient.builder()
            .baseUrl(consoleProperties.gateway().baseUrl())
            .build();
    }

}
