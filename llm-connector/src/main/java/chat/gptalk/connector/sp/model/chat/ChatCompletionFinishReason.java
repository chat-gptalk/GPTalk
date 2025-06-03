package chat.gptalk.connector.sp.model.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ChatCompletionFinishReason {

    /**
     * The model hit a natural stop point or a provided stop sequence.
     */
    @JsonProperty("stop")
    STOP,
    /**
     * The maximum number of tokens specified in the request was reached.
     */
    @JsonProperty("length")
    LENGTH,
    /**
     * The content was omitted due to a flag from our content filters.
     */
    @JsonProperty("content_filter")
    CONTENT_FILTER,
    /**
     * The model called a tool.
     */
    @JsonProperty("tool_calls")
    TOOL_CALLS,
    /**
     * Only for compatibility with Mistral AI API.
     */
    @JsonProperty("tool_call")
    TOOL_CALL
}
