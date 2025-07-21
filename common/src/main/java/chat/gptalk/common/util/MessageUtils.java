package chat.gptalk.common.util;

import chat.gptalk.common.constants.ErrorCode;
import chat.gptalk.common.constants.FieldI18nKey;
import java.util.Arrays;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageUtils {

    private static MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        MessageUtils.messageSource = messageSource;
    }

    public static String resolveMessage(Locale locale, ErrorCode key) {
        try {
            return messageSource.getMessage(key.name(), null, locale);
        } catch (Exception e) {
            return key.name();
        }
    }

    public static String resolveMessage(Locale locale, String key, Object[] args) {
        try {
            return messageSource.getMessage(key, args, Locale.getDefault());
        } catch (Exception e) {
            return key;
        }
    }

    public static String resolveMessage(Locale locale, ErrorCode errorCode, Object[] args) {
        try {
            Object[] localizedArgs = Arrays.stream(args)
                .map(arg -> {
                    if (arg instanceof String && ((String) arg).startsWith(FieldI18nKey.PREFIX)) {
                        return messageSource.getMessage((String) arg, null, locale);
                    }
                    return arg;
                })
                .toArray();
            return messageSource.getMessage(errorCode.name(), localizedArgs, Locale.getDefault());
        } catch (Exception e) {
            return errorCode.name();
        }
    }
}
