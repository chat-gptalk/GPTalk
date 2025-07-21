package chat.gptalk.connector.model;

import java.util.Map;
import java.util.UUID;

public record LlmProviderKey(
    UUID providerKeyId,
    Map<String, String> credential) {


    public String getApiKey() {
        return credential.get("api_key");
    }
}
