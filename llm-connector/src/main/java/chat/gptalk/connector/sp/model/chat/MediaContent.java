package chat.gptalk.connector.sp.model.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public record MediaContent(
    @JsonProperty("type") String type,
    @JsonProperty("text") String text,
    @JsonProperty("image_url") ImageUrl imageUrl,
    @JsonProperty("input_audio") InputAudio inputAudio) {

    /**
     * Shortcut constructor for a text content.
     *
     * @param text The text content of the message.
     */
    public MediaContent(String text) {
        this("text", text, null, null);
    }

    /**
     * Shortcut constructor for an image content.
     *
     * @param imageUrl The image content of the message.
     */
    public MediaContent(
        ImageUrl imageUrl) {
        this("image_url", null, imageUrl, null);
    }

    /**
     * Shortcut constructor for an audio content.
     *
     * @param inputAudio The audio content of the message.
     */
    public MediaContent(
        InputAudio inputAudio) {
        this("input_audio", null, null, inputAudio);
    }

    /**
     * @param data Base64 encoded audio data.
     * @param format The format of the encoded audio data. Currently supports "wav" and "mp3".
     */
    @JsonInclude(Include.NON_NULL)
    public record InputAudio(
        @JsonProperty("data") String data,
        @JsonProperty("format") Format format) {

        public enum Format {
            /**
             * MP3 audio format
             */
            @JsonProperty("mp3") MP3,
            /**
             * WAV audio format
             */
            @JsonProperty("wav") WAV
        }
    }

    /**
     * Shortcut constructor for an image content.
     *
     * @param url Either a URL of the image or the base64 encoded image data. The base64 encoded image data must
     * have a special prefix in the following format: "data:{mimetype};base64,{base64-encoded-image-data}".
     * @param detail Specifies the detail level of the image.
     */
    @JsonInclude(Include.NON_NULL)
    public record ImageUrl(@JsonProperty("url") String url, @JsonProperty("detail") String detail) {

        public ImageUrl(String url) {
            this(url, null);
        }

    }

}
