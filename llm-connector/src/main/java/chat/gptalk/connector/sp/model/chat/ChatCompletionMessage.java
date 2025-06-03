package chat.gptalk.connector.sp.model.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
public record ChatCompletionMessage(
    @JsonProperty("content") Object rawContent,
    @JsonProperty("role") Role role,
    @JsonProperty("name") String name,
    @JsonProperty("tool_call_id") String toolCallId,
    @JsonProperty("tool_calls")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY) List<ToolCall> toolCalls,
    @JsonProperty("refusal") String refusal,
    @JsonProperty("audio") AudioOutput audioOutput) {

    /**
     * Create a chat completion message with the given content and role. All other fields are null.
     *
     * @param content The contents of the message.
     * @param role The role of the author of this message.
     */
    public ChatCompletionMessage(Object content, Role role) {
        this(content, role, null, null, null, null, null);

    }

    /**
     * Get message content as String.
     */
    public String content() {
        if (this.rawContent == null) {
            return null;
        }
        if (this.rawContent instanceof String text) {
            return text;
        }
        throw new IllegalStateException("The content is not a string!");
    }

    /**
     * The role of the author of this message.
     */
    public enum Role {

        @JsonProperty("developer")
        DEVELOPER,

        /**
         * System message.
         */
        @JsonProperty("system")
        SYSTEM,
        /**
         * User message.
         */
        @JsonProperty("user")
        USER,
        /**
         * Assistant message.
         */
        @JsonProperty("assistant")
        ASSISTANT,
        /**
         * Tool message.
         */
        @JsonProperty("tool")
        TOOL

    }

    @JsonInclude(Include.NON_NULL)
    public record ToolCall(
        @JsonProperty("index") Integer index,
        @JsonProperty("id") String id,
        @JsonProperty("type") String type,
        @JsonProperty("function") ChatCompletionFunction function) {

        public ToolCall(String id, String type, ChatCompletionFunction function) {
            this(null, id, type, function);
        }

    }

    @Setter
    @Getter
    @JsonInclude(Include.NON_NULL)
    public static class FunctionTool {

        @JsonProperty("type")
        private Type type = Type.FUNCTION;

        @JsonProperty("function")
        private Function function;
        public enum Type {
            @JsonProperty("function")
            FUNCTION

        }

        @JsonInclude(Include.NON_NULL)
        public record Function(
            @JsonProperty("description")
            String description,

            @JsonProperty("name")
            String name,

            @JsonProperty("parameters")
            Map<String, Object> parameters,

            @JsonProperty("strict")
            Boolean strict,
            @JsonIgnore
            String jsonSchema
        ) {
        }

    }


    /**
     * The function definition.
     *
     * @param name The name of the function.
     * @param arguments The arguments that the model expects you to pass to the function.
     */
    @JsonInclude(Include.NON_NULL)
    public record ChatCompletionFunction(
        @JsonProperty("name") String name,
        @JsonProperty("arguments") String arguments) {

    }

    /**
     * Audio normalizedResponse from the model.
     *
     * @param id Unique identifier for the audio normalizedResponse from the model.
     * @param data Audio output from the model.
     * @param expiresAt When the audio content will no longer be available on the server.
     * @param transcript Transcript of the audio output from the model.
     */
    @JsonInclude(Include.NON_NULL)
    public record AudioOutput(
        @JsonProperty("id") String id,
        @JsonProperty("data") String data,
        @JsonProperty("expires_at") Long expiresAt,
        @JsonProperty("transcript") String transcript
    ) {

    }
}