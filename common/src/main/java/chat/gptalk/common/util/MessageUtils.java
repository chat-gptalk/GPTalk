package chat.gptalk.common.util;

import chat.gptalk.common.exception.ApiException.ErrorDetail;
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

    public static String getMessage(ErrorDetail detail) {
        try {
            return messageSource.getMessage(detail.messageKey(), detail.messageArgs(), Locale.getDefault());
        } catch (Exception e) {
            return detail.messageKey();
        }
    }

    public static String getMessage(String key) {
        try {
            return messageSource.getMessage(key, null, Locale.getDefault());
        } catch (Exception e) {
            return key;
        }
    }

    public static String getMessage(String key, Object[] args) {
        try {
            return messageSource.getMessage(key, args, Locale.getDefault());
        } catch (Exception e) {
            return key;
        }
    }
}
