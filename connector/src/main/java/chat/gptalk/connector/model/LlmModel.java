package chat.gptalk.connector.model;

import java.util.UUID;

public record LlmModel(
    UUID providerId,
    UUID modelId,
    String name) {

}
