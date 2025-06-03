package chat.gptalk.auth.config;

import chat.gptalk.common.config.CryptoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
    CryptoProperties.class,
    ConsoleProperties.class,
    AuthProperties.class
})
public class ConfigRegistrar {

}
