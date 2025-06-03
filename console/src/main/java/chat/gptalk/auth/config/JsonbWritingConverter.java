package chat.gptalk.auth.config;

import chat.gptalk.common.util.JsonUtils;
import java.sql.SQLException;
import java.util.Map;
import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class JsonbWritingConverter implements Converter<Map<String, Object>, PGobject> {

    @Override
    public PGobject convert(Map<String, Object> source) {
        PGobject pGobject = new PGobject();
        pGobject.setType("jsonb");
        try {
            pGobject.setValue(JsonUtils.toJson(source));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pGobject;
    }
}
