package chat.gptalk.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.KotlinDetector;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

    private static ObjectMapper objectMapper;
    private static ObjectMapper objectMapperWithoutNull;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        JsonUtils.objectMapper = objectMapper;
        JsonUtils.objectMapperWithoutNull = objectMapper.copy();
        JsonUtils.objectMapperWithoutNull.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        if (!StringUtils.hasLength(json)) {
            throw new RuntimeException("json is empty");
        }
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObject(String json, TypeReference<T> typeReference) {
        if (!StringUtils.hasLength(json)) {
            return null;
        }
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static <T> String toJson(T obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> String toJsonWithoutNull(T obj) {
        try {
            return objectMapperWithoutNull.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Object> toMap(Object obj) {
        return objectMapper.convertValue(obj, Map.class);
    }

    public static <T> T convert(Object obj, Class<T> clazz) {
        return objectMapper.convertValue(obj, clazz);
    }

    public static <T> T convert(Object obj, TypeReference<T> typeReference) {
        return objectMapper.convertValue(obj, typeReference);
    }


    public static Boolean isJson(String json) {
        try {
            objectMapper.readTree(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static List<Module> instantiateAvailableModules() {
        List<Module> modules = new ArrayList<>();
        try {
            Class<? extends Module> jdk8ModuleClass = (Class<? extends Module>) ClassUtils
                .forName("com.fasterxml.jackson.datatype.jdk8.Jdk8Module", null);
            Module jdk8Module = BeanUtils.instantiateClass(jdk8ModuleClass);
            modules.add(jdk8Module);
        } catch (ClassNotFoundException ex) {
            // jackson-datatype-jdk8 not available
        }

        try {
            Class<? extends Module> javaTimeModuleClass = (Class<? extends Module>) ClassUtils
                .forName("com.fasterxml.jackson.datatype.jsr310.JavaTimeModule", null);
            Module javaTimeModule = BeanUtils.instantiateClass(javaTimeModuleClass);
            modules.add(javaTimeModule);
        } catch (ClassNotFoundException ex) {
            // jackson-datatype-jsr310 not available
        }

        try {
            Class<? extends Module> parameterNamesModuleClass = (Class<? extends Module>) ClassUtils
                .forName("com.fasterxml.jackson.module.paramnames.ParameterNamesModule", null);
            Module parameterNamesModule = BeanUtils
                .instantiateClass(parameterNamesModuleClass);
            modules.add(parameterNamesModule);
        } catch (ClassNotFoundException ex) {
            // jackson-module-parameter-names not available
        }

        // Kotlin present?
        if (KotlinDetector.isKotlinPresent()) {
            try {
                Class<? extends Module> kotlinModuleClass = (Class<? extends Module>) ClassUtils
                    .forName("com.fasterxml.jackson.module.kotlin.KotlinModule", null);
                Module kotlinModule = BeanUtils.instantiateClass(kotlinModuleClass);
                modules.add(kotlinModule);
            } catch (ClassNotFoundException ex) {
                // jackson-module-kotlin not available
            }
        }
        return modules;
    }
}
