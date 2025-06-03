package chat.gptalk.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeanMapperUtils {

    private static ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        BeanMapperUtils.objectMapper = objectMapper;
    }

    public static <T> T map(Object from, Class<T> clazz) {
        return objectMapper.convertValue(from, clazz);
    }

}
