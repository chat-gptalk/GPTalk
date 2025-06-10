package chat.gptalk.connector.router;

import chat.gptalk.common.crypto.CryptoProvider;
import chat.gptalk.common.exception.NotFoundException;
import chat.gptalk.connector.model.ModelConfig;
import chat.gptalk.connector.model.Provider;
import chat.gptalk.connector.model.ProviderKey;
import chat.gptalk.connector.model.ProviderModel;
import chat.gptalk.connector.model.VirtualModel;
import chat.gptalk.connector.repository.LlmModelRepository;
import chat.gptalk.connector.repository.LlmProviderKeyRepository;
import chat.gptalk.connector.repository.LlmProviderRepository;
import chat.gptalk.connector.repository.VirtualModelRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ModelRouter {

    private final ModelRoutingStrategy modelRoutingStrategy;
    private final LlmModelRepository modelRepository;
    private final CryptoProvider cryptoProvider;
    private final LlmProviderRepository providerRepository;
    private final LlmProviderKeyRepository providerKeyRepository;
    private final VirtualModelRepository virtualModelRepository;

    public Mono<ModelConfig> resolve(UUID tenantId, String vmName) {
        return virtualModelRepository.findByName(tenantId, vmName)
            .switchIfEmpty(Mono.error(new NotFoundException("Virtual model: " + vmName + " not found")))
            .flatMap(virtualModelEntity -> virtualModelRepository.findModelsByVirtualModelId(tenantId,
                    virtualModelEntity.virtualModelId())
                .map(models -> models.stream().map(m -> new ProviderModel(m.providerId(), m.modelId(), m.name()))
                    .toList())
                .switchIfEmpty(Mono.error(new NotFoundException("Virtual model: " + vmName + " not bind any models")))
                .flatMap(models -> modelRoutingStrategy.select(tenantId, models))
                .flatMap(model -> providerRepository.findByProviderId(model.providerId())
                    .map(provider -> new Provider(provider.providerId(), provider.baseUrl(), provider.sdkClientClass()))
                    .flatMap(provider -> providerKeyRepository
                        .findFirstByProviderId(tenantId, provider.providerId())
                        .map(it -> ModelConfig
                            .builder()
                            .providerModel(model)
                            .provider(provider)
                            .providerKey(new ProviderKey(it.providerKeyId(), cryptoProvider.decrypt(it.keyEnc())))
                            .virtualModel(
                                new VirtualModel(virtualModelEntity.virtualModelId(), virtualModelEntity.name()))
                            .build()
                        )
                    )
                )
            );
    }
}
