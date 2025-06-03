package chat.gptalk.common.config;

import org.springframework.context.MessageSource;

public interface MessageCustomConfigurer {

    MessageSource customResourceBundle();
}
