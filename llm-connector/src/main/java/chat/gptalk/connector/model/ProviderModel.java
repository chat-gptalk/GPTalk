package chat.gptalk.connector.model;

import java.util.UUID;

public record ProviderModel(
    UUID providerId,
    UUID providerModelId,
    String providerModelName) {

}
