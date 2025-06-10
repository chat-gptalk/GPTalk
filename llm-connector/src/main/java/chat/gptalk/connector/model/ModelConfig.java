package chat.gptalk.connector.model;

import lombok.Builder;

@Builder
public record ModelConfig(
    Provider provider,
    ProviderKey providerKey,
    VirtualModel virtualModel,
    ProviderModel providerModel
) {

}
