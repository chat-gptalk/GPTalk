package chat.gptalk.connector.sp.model.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public record LogProbs(
    @JsonProperty("content") List<Content> content,
    @JsonProperty("refusal") List<Content> refusal) {

    /**
     * Message content tokens with log probability information.
     *
     * @param token The token.
     * @param logprob The log probability of the token.
     * @param probBytes A list of integers representing the UTF-8 bytes representation of the token. Useful in instances
     * where characters are represented by multiple tokens and their byte representations must be combined to generate
     * the correct text representation. Can be null if there is no bytes representation for the token.
     * @param topLogprobs List of the most likely tokens and their log probability, at this token position. In rare
     * cases, there may be fewer than the number of requested top_logprobs returned.
     */
    @JsonInclude(Include.NON_NULL)
    public record Content(
        @JsonProperty("token") String token,
        @JsonProperty("logprob") Float logprob,
        @JsonProperty("bytes") List<Integer> probBytes,
        @JsonProperty("top_logprobs") List<TopLogProbs> topLogprobs) {

        /**
         * The most likely tokens and their log probability, at this token position.
         *
         * @param token The token.
         * @param logprob The log probability of the token.
         * @param probBytes A list of integers representing the UTF-8 bytes representation of the token. Useful in
         * instances where characters are represented by multiple tokens and their byte representations must be combined
         * to generate the correct text representation. Can be null if there is no bytes representation for the token.
         */
        @JsonInclude(Include.NON_NULL)
        public record TopLogProbs(
            @JsonProperty("token") String token,
            @JsonProperty("logprob") Float logprob,
            @JsonProperty("bytes") List<Integer> probBytes) {

        }

    }
}
