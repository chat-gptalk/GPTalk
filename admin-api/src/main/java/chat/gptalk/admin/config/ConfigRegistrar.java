package chat.gptalk.admin.config;

import chat.gptalk.common.config.CryptoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
    CryptoProperties.class,
    ConsoleProperties.class
})
public class ConfigRegistrar {

}
