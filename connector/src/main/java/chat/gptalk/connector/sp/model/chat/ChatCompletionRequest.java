package chat.gptalk.connector.sp.model.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.With;

@JsonInclude(Include.NON_NULL)
@With
public record ChatCompletionRequest(
    @JsonProperty("messages")
    List<ChatCompletionMessage> messages,
    @JsonProperty("model")
    String model,
    @JsonProperty("store")
    Boolean store,
    @JsonProperty("metadata")
    Map<String, String> metadata,
    @JsonProperty("frequency_penalty")
    Double frequencyPenalty,
    @JsonProperty("logit_bias")
    Map<String, Integer> logitBias,
    @JsonProperty("logprobs")
    Boolean logprobs,
    @JsonProperty("top_logprobs")
    Integer topLogprobs,
    @JsonProperty("max_tokens")
    @Deprecated
    Integer maxTokens, // Use maxCompletionTokens instead
    @JsonProperty("max_completion_tokens")
    Integer maxCompletionTokens,
    @JsonProperty("n")
    Integer n,
    @JsonProperty("modalities")
    List<OutputModality> outputModalities,
    @JsonProperty("audio")
    AudioParameters audioParameters,
    @JsonProperty("presence_penalty")
    Double presencePenalty,
    @JsonProperty("response_format")
    ResponseFormat responseFormat,
    @JsonProperty("seed")
    Integer seed,
    @JsonProperty("service_tier")
    String serviceTier,
    @JsonProperty("stop")
    List<String> stop,
    @JsonProperty("stream")
    Boolean stream,
    @JsonProperty("stream_options")
    StreamOptions streamOptions,
    @JsonProperty("temperature")
    Double temperature,
    @JsonProperty("top_p")
    Double topP,
    @JsonProperty("tools")
    List<ChatCompletionMessage.FunctionTool> tools,
    @JsonProperty("tool_choice")
    Object toolChoice,
    @JsonProperty("parallel_tool_calls")
    Boolean parallelToolCalls,
    @JsonProperty("user")
    String user,
    @JsonProperty("reasoning_effort")
    String reasoningEffort,
    @JsonProperty("model_options")
    Map<String, Object> modelOptions) {

    public enum OutputModality {
        @JsonProperty("audio")
        AUDIO,
        @JsonProperty("text")
        TEXT
    }

    @JsonInclude(Include.NON_NULL)
    public record AudioParameters(
        @JsonProperty("voice") Voice voice,
        @JsonProperty("format") AudioResponseFormat format) {

        /**
         * Specifies the voice type.
         */
        public enum Voice {
            /**
             * Alloy voice
             */
            @JsonProperty("alloy") ALLOY,
            /**
             * Echo voice
             */
            @JsonProperty("echo") ECHO,
            /**
             * Fable voice
             */
            @JsonProperty("fable") FABLE,
            /**
             * Onyx voice
             */
            @JsonProperty("onyx") ONYX,
            /**
             * Nova voice
             */
            @JsonProperty("nova") NOVA,
            /**
             * Shimmer voice
             */
            @JsonProperty("shimmer") SHIMMER
        }

        /**
         * Specifies the output audio format.
         */
        public enum AudioResponseFormat {
            /**
             * MP3 format
             */
            @JsonProperty("mp3") MP3,
            /**
             * FLAC format
             */
            @JsonProperty("flac") FLAC,
            /**
             * OPUS format
             */
            @JsonProperty("opus") OPUS,
            /**
             * PCM16 format
             */
            @JsonProperty("pcm16") PCM16,
            /**
             * WAV format
             */
            @JsonProperty("wav") WAV
        }
    }

    @JsonInclude(Include.NON_NULL)
    public record StreamOptions(
        @JsonProperty("include_usage") Boolean includeUsage) {

        public static StreamOptions INCLUDE_USAGE = new StreamOptions(true);
    }
}