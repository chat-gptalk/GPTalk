package chat.gptalk.connector.model;

import java.util.UUID;

public record ProviderKey(
    UUID providerKeyId,
    String key) {

}
