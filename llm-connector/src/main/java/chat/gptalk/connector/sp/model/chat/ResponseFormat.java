package chat.gptalk.connector.sp.model.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

@JsonInclude(Include.NON_NULL)
public record ResponseFormat(
    /**
     * Type Must be one of 'text', 'json_object' or 'json_schema'.
     */
    @JsonProperty("type") Type type,

    /**
     * JSON schema object that describes the format of the JSON object. Only applicable when type is 'json_schema'.
     */
    @JsonProperty("json_schema") JsonSchema jsonSchema,

    String schema
) {


    public enum Type {

        /**
         * Generates a text normalizedResponse. (default)
         */
        @JsonProperty("text")
        TEXT,

        /**
         * Enables JSON mode, which guarantees the message the model generates is valid JSON.
         */
        @JsonProperty("json_object")
        JSON_OBJECT,

        /**
         * Enables Structured Outputs which guarantees the model will match your supplied JSON schema.
         */
        @JsonProperty("json_schema")
        JSON_SCHEMA

    }

    /**
     * JSON schema object that describes the format of the JSON object. Applicable for the 'json_schema' type only.
     */
    @JsonInclude(Include.NON_NULL)
    public record JsonSchema(
        @JsonProperty("name") String name,
        @JsonProperty("schema") Map<String, Object> schema,
        @JsonProperty("strict") Boolean strict
    ) {
    }

}
