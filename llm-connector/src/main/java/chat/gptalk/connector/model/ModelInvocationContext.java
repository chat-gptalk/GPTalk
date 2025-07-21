package chat.gptalk.connector.model;

public record ModelInvocationContext(
    ModelMeta meta,
    LlmProviderKey providerKey
) {
}
