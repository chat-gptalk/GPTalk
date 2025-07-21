package chat.gptalk.connector.router;

import chat.gptalk.common.constants.ErrorCode;
import chat.gptalk.common.constants.FieldI18nKey;
import chat.gptalk.common.crypto.CryptoProvider;
import chat.gptalk.common.entity.LlmModelEntity;
import chat.gptalk.common.entity.LlmProviderEntity;
import chat.gptalk.common.entity.LlmProviderKeyEntity;
import chat.gptalk.common.entity.VirtualModelEntity;
import chat.gptalk.common.exception.BizException;
import chat.gptalk.common.util.JsonUtils;
import chat.gptalk.connector.model.LlmModel;
import chat.gptalk.connector.model.LlmProvider;
import chat.gptalk.connector.model.LlmProviderKey;
import chat.gptalk.connector.model.ModelMeta;
import chat.gptalk.connector.model.VirtualModel;
import chat.gptalk.connector.repository.LlmProviderKeyRepository;
import chat.gptalk.connector.repository.LlmProviderRepository;
import chat.gptalk.connector.repository.VirtualModelRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ModelRouter {

    private final ModelRoutingStrategy modelRoutingStrategy;
    private final CryptoProvider cryptoProvider;
    private final LlmProviderRepository providerRepository;
    private final LlmProviderKeyRepository providerKeyRepository;
    private final VirtualModelRepository virtualModelRepository;

    public Mono<ModelMeta> resolveMeta(UUID tenantId, String vmName) {
        return virtualModelRepository.findByName(tenantId, vmName)
            .switchIfEmpty(Mono.error(new BizException(ErrorCode.CM_NOT_FOUND, FieldI18nKey.V_MODEL, vmName)))
            .flatMap(virtualModelEntity -> virtualModelRepository.findModelsByVirtualModelId(tenantId,
                    virtualModelEntity.virtualModelId())
                .map(this::toModels)
                .switchIfEmpty(Mono.error(new BizException(ErrorCode.CN_VMODEL_NOT_BIND)))
                .flatMap(models -> selectModel(tenantId, models))
                .flatMap(model -> providerRepository.findByProviderId(model.providerId())
                    .map(this::toProvider)
                    .map(provider -> toMeta(virtualModelEntity, model, provider)
                    )
                )
            );
    }

    private LlmProvider toProvider(LlmProviderEntity provider) {
        return new LlmProvider(provider.providerId(), provider.baseUrl(), provider.sdkClientClass());
    }

    private Mono<LlmModel> selectModel(UUID tenantId, List<LlmModel> models) {
        return modelRoutingStrategy.select(tenantId, models);
    }

    private List<LlmModel> toModels(List<LlmModelEntity> models) {
        return models.stream()
            .map(m -> new LlmModel(m.providerId(), m.modelId(), m.name()))
            .toList();
    }

    private ModelMeta toMeta(VirtualModelEntity virtualModelEntity, LlmModel model, LlmProvider provider) {
        return ModelMeta
            .builder()
            .model(model)
            .provider(provider)
            .virtualModel(new VirtualModel(virtualModelEntity.virtualModelId(), virtualModelEntity.name()))
            .build();
    }

    public Mono<LlmProviderKey> selectKey(UUID tenantId, UUID providerId, UUID modelId) {
        return providerRepository.findByProviderId(providerId)
            .flatMap(provider -> providerKeyRepository
                .findFirstByProviderId(tenantId, provider.providerId())
                .map(this::buildKey)
            );
    }

    private LlmProviderKey buildKey(LlmProviderKeyEntity providerKeyEntity) {
        String decrypted = cryptoProvider.decrypt(providerKeyEntity.keyEnc());
        Map<String, String> credential = JsonUtils.toObject(decrypted, Map.class);
        return new LlmProviderKey(providerKeyEntity.providerKeyId(), credential);
    }
}
