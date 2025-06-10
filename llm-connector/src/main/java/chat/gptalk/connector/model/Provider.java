package chat.gptalk.connector.model;

import java.util.UUID;

public record Provider(
    UUID providerId,
    String baseUrl,
    String sdkClass) {

}
