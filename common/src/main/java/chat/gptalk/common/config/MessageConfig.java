package chat.gptalk.common.config;

import chat.gptalk.common.util.SpringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

public class MessageConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:common/i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");

        CompositeMessageSource compositeMessageSource = new CompositeMessageSource();
        compositeMessageSource.addMessageSource(messageSource);
        SpringUtils.getBeansOfType(MessageCustomConfigurer.class)
            .forEach(custom -> compositeMessageSource.addMessageSource(custom.customResourceBundle()));
        return compositeMessageSource;
    }

    public static class CompositeMessageSource implements MessageSource {

        private final List<MessageSource> messageSources = new ArrayList<>();

        @Override
        public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
            for (MessageSource messageSource : messageSources) {
                try {
                    return messageSource.getMessage(code, args, defaultMessage, locale);
                } catch (NoSuchMessageException ignore) {
                }
            }
            throw new NoSuchMessageException(code, locale);
        }

        @Override
        public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
            for (MessageSource messageSource : messageSources) {
                try {
                    return messageSource.getMessage(code, args, locale);
                } catch (NoSuchMessageException ignore) {
                }
            }
            throw new NoSuchMessageException(code, locale);
        }

        @Override
        public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
            for (MessageSource messageSource : messageSources) {
                try {
                    return messageSource.getMessage(resolvable, locale);
                } catch (NoSuchMessageException ignore) {
                }
            }
            String[] codes = resolvable.getCodes();
            String code = (codes != null && codes.length > 0 ? codes[0] : "");
            throw new NoSuchMessageException(code, locale);
        }

        public void addMessageSource(MessageSource messageSource) {
            messageSources.add(messageSource);
        }
    }
}
