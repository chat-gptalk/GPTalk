package chat.gptalk.auth.service;

import chat.gptalk.auth.model.response.ProviderResponse;
import chat.gptalk.auth.model.response.TreeNode;
import chat.gptalk.auth.repository.ModelRepository;
import chat.gptalk.auth.repository.ProviderRepository;
import chat.gptalk.auth.util.SecurityUtils;
import chat.gptalk.common.entity.LlmModelEntity;
import chat.gptalk.common.entity.LlmProviderEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProviderService {

    private final ModelRepository modelRepository;
    private final ProviderRepository providerRepository;

    public List<ProviderResponse> getCurrentTenantProviders() {
        return providerRepository.findByTenantId(SecurityUtils.getCurrentUser().tenantId())
            .stream()
            .map(this::mapToResponse)
            .toList();
    }

    public ProviderResponse findByProviderId(UUID providerId) {
        LlmProviderEntity entity = providerRepository.findByProviderId(providerId);
        return mapToResponse(entity);
    }

    private ProviderResponse mapToResponse(LlmProviderEntity entity) {
        return ProviderResponse.builder()
            .name(entity.name())
            .baseUrl(entity.baseUrl())
            .providerId(entity.providerId())
            .extraConfig(entity.extraConfig())
            .sdkClientClass(entity.sdkClientClass())
            .system(entity.system())
            .enabled(entity.enabled())
            .modelCount(modelRepository.countByProviderId(entity.providerId()))
            .createdAt(entity.createdAt())
            .updatedAt(entity.updatedAt())
            .build()
            ;
    }

    public List<TreeNode> getProviderTree() {
        Iterable<LlmProviderEntity> providers = providerRepository.findAll();
        List<LlmModelEntity> models = modelRepository.findAll();

        List<TreeNode> treeNodes = new ArrayList<>();
        providers.forEach(provider -> {
            List<LlmModelEntity> providerModels = models.stream()
                .filter(it -> it.providerId().equals(provider.providerId()))
                .toList();
            treeNodes.add(TreeNode.builder()
                .title(provider.name())
                .value(provider.providerId().toString())
                .key(provider.providerId().toString())
                .children(buildModelTree(providerModels))
                .selectable(false)
                .build());
        });
        return treeNodes;
    }

    private List<TreeNode> buildModelTree(List<LlmModelEntity> models) {
        return models.stream()
            .map(model -> TreeNode.builder()
                .title(model.name())
                .value(model.modelId().toString())
                .key(model.modelId().toString())
                .selectable(true)
                .build()).toList();

    }
}
