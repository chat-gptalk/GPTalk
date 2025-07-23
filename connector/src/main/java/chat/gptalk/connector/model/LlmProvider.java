package chat.gptalk.connector.model;

import java.util.UUID;

public record LlmProvider(
    UUID providerId,
    String baseUrl,
    String sdkClass) {

}
