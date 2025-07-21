package chat.gptalk.auth.config.jdbc;

import chat.gptalk.common.util.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Map;
import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class JsonbReadingConverter implements Converter<PGobject, Map<String, Object>> {

    @Override
    public Map<String, Object> convert(PGobject source) {
        return JsonUtils.toObject(source.getValue(), new TypeReference<Map<String, Object>>() {});
    }
}
