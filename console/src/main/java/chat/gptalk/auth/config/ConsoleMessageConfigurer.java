package chat.gptalk.auth.config;

import chat.gptalk.common.config.MessageCustomConfigurer;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
public class ConsoleMessageConfigurer implements MessageCustomConfigurer {

    @Override
    public MessageSource customResourceBundle() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
