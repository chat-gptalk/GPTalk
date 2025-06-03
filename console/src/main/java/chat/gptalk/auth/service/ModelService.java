package chat.gptalk.auth.service;

import chat.gptalk.auth.model.response.ModelResponse;
import chat.gptalk.auth.repository.ModelRepository;
import chat.gptalk.auth.util.SecurityUtils;
import chat.gptalk.common.entity.LlmModelEntity;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModelService {

    private final ModelRepository modelRepository;
    private final ProviderService providerService;

    public List<ModelResponse> getModels() {
        return modelRepository.findByTenantId(SecurityUtils.getCurrentUser().tenantId())
            .stream()
            .map(this::mapToResponse)
            .toList();
    }

    public ModelResponse findByModelId(UUID modelId) {
        LlmModelEntity modelEntity = modelRepository.findByModelId(modelId);
        return mapToResponse(modelEntity);
    }

    public List<ModelResponse> getProviderModels(String providerId) {
        return modelRepository.findByProviderId(UUID.fromString(providerId))
            .stream()
            .map(this::mapToResponseWithoutProvider)
            .toList();
    }

    private ModelResponse mapToResponse(LlmModelEntity entity) {
        return mapToResponseWithoutProvider(entity)
            .withProvider(providerService.findByProviderId(entity.providerId()));
    }

    private ModelResponse mapToResponseWithoutProvider(LlmModelEntity entity) {
        return ModelResponse.builder()
            .modelId(entity.modelId())
            .enabled(entity.enabled())
            .name(entity.name())
            .features(entity.features())
            .contextLength(entity.contextLength())
            .defaultParams(entity.defaultParams())
            .maxOutputTokens(entity.maxOutputTokens())
            .createdAt(entity.createdAt())
            .updatedAt(entity.updatedAt())
            .build();
    }
}
