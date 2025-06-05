package chat.gptalk.auth.service;

import chat.gptalk.auth.model.request.CreateProviderRequest;
import chat.gptalk.auth.model.response.ProviderResponse;
import chat.gptalk.auth.model.response.TreeNode;
import chat.gptalk.auth.repository.ModelRepository;
import chat.gptalk.auth.repository.ProviderRepository;
import chat.gptalk.auth.util.SecurityUtils;
import chat.gptalk.common.entity.LlmModelEntity;
import chat.gptalk.common.entity.LlmProviderEntity;
import chat.gptalk.common.exception.DataConflictException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProviderService {

    private final ModelRepository modelRepository;
    private final ProviderRepository providerRepository;

    public List<ProviderResponse> getCurrentTenantProviders() {
        return providerRepository.findByTenantIdOrderByIdDesc(SecurityUtils.getTenantId())
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
            .description(entity.description())
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

    public ProviderResponse createProvider(@Valid CreateProviderRequest createProviderRequest) {
        if (providerRepository.existsByNameAndTenantId(createProviderRequest.name(),
            SecurityUtils.getTenantId())) {
            throw new DataConflictException("Provider already exists");
        }
        LlmProviderEntity providerEntity = LlmProviderEntity.builder()
            .providerId(UUID.randomUUID())
            .name(createProviderRequest.name())
            .baseUrl(createProviderRequest.baseUrl())
            .sdkClientClass(createProviderRequest.sdkClientClass())
            .extraConfig(createProviderRequest.extraConfig())
            .description(createProviderRequest.description())
            .system(false)
            .enabled(true)
            .userId(SecurityUtils.getCurrentUser().userId())
            .tenantId(SecurityUtils.getTenantId())
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build();
        providerRepository.save(providerEntity);
        return mapToResponse(providerEntity);
    }

    public boolean hasPermission(@NotNull String id) {
        return hasPermissions(new String[]{id});
    }

    public boolean hasPermissions(@NotNull String[] ids) {
        List<LlmProviderEntity> results = providerRepository.findAllByTenantIdAndProviderIdIn(
            SecurityUtils.getTenantId(),
            Arrays.stream(ids).map(UUID::fromString).toList());
        return results.size() == ids.length;
    }

    @Transactional
    public void batchDelete(@NotNull String[] ids) {
        providerRepository.deleteByProviderIdIn(Arrays.stream(ids).map(UUID::fromString).toList());
    }

    public ProviderResponse getProvider(String providerId) {
        LlmProviderEntity provider = providerRepository.findOneByTenantIdAndProviderIdOrSystem(
            SecurityUtils.getTenantId(), UUID.fromString(providerId), true);
        return mapToResponse(provider);
    }
}
