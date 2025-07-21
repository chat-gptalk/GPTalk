package chat.gptalk.security.config;

import chat.gptalk.security.JwksManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

public class CommonSecurityConfig {

    @Bean
    @ConditionalOnBean(WebClient.class)
    @ConditionalOnProperty("gptalk.auth.baseUrl")
    public JwksManager jwksManager(WebClient webClient, @Value("${gptalk.auth.baseUrl:}") String baseUrl) {
        return new JwksManager(webClient, baseUrl);
    }
}
