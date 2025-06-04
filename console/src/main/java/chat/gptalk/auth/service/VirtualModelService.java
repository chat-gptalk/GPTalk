package chat.gptalk.auth.service;

import chat.gptalk.auth.model.request.CreateVirtualModelRequest;
import chat.gptalk.auth.model.response.ModelResponse;
import chat.gptalk.auth.model.response.VirtualModelResponse;
import chat.gptalk.auth.repository.VirtualModelMappingRepository;
import chat.gptalk.auth.repository.VirtualModelRepository;
import chat.gptalk.auth.util.SecurityUtils;
import chat.gptalk.common.entity.VirtualModelEntity;
import chat.gptalk.common.entity.VirtualModelMappingEntity;
import chat.gptalk.common.exception.DataConflictException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VirtualModelService {

    private final ModelService modelService;
    private final VirtualModelRepository virtualModelRepository;
    private final VirtualModelMappingRepository virtualModelMappingRepository;

    public List<VirtualModelResponse> getModels() {
        return virtualModelRepository.findAll(Sort.by(Order.desc("id"))).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    public VirtualModelResponse createModel(@Valid CreateVirtualModelRequest createVirtualModelRequest) {
        String name = createVirtualModelRequest.name();
        if (!createVirtualModelRequest.name().startsWith("vm:")) {
            name = "vm:" + name;
        }
        if (virtualModelRepository.existsByTenantIdAndName(SecurityUtils.getCurrentUser().tenantId(), name)) {
            throw new DataConflictException("Model " + name + " already exists");
        }
        UUID virtualModelId = UUID.randomUUID();
        VirtualModelEntity virtualModelEntity = VirtualModelEntity.builder()
            .name(name)
            .description(createVirtualModelRequest.description())
            .virtualModelId(virtualModelId)
            .enabled(true)
            .userId(SecurityUtils.getCurrentUser().userId())
            .tenantId(SecurityUtils.getCurrentUser().tenantId())
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build();
        virtualModelEntity = virtualModelRepository.save(virtualModelEntity);
        List<VirtualModelMappingEntity> mappings = createVirtualModelRequest.modelIds().stream()
            .map(modelId -> VirtualModelMappingEntity.builder()
                .modelId(UUID.fromString(modelId))
                .virtualModelId(virtualModelId)
                .userId(SecurityUtils.getCurrentUser().userId())
                .tenantId(SecurityUtils.getCurrentUser().tenantId())
                .weight(0)
                .priority(0)
                .build())
            .toList();
        virtualModelMappingRepository.saveAll(mappings);
        return mapToResponse(virtualModelEntity);
    }

    private VirtualModelResponse mapToResponse(VirtualModelEntity entity) {
        List<VirtualModelResponse.MappedModelResponse> mappings = virtualModelMappingRepository.findByVirtualModelId(
                entity.virtualModelId())
            .stream().map(mapping -> {
                ModelResponse model = modelService.findByModelId(mapping.modelId());
                return VirtualModelResponse.MappedModelResponse
                    .builder()
                    .model(model)
                    .weight(mapping.weight())
                    .priority(mapping.priority())
                    .build();
            }).toList();
        return VirtualModelResponse
            .builder()
            .name(entity.name())
            .virtualModelId(entity.virtualModelId())
            .enabled(entity.enabled())
            .models(mappings)
            .createdAt(entity.createdAt())
            .updatedAt(entity.updatedAt())
            .build();
    }

    @Transactional
    public void batchDelete(@NotNull String[] ids) {
        virtualModelRepository.deleteByVirtualModelIdIn(Arrays.stream(ids).map(UUID::fromString).toList());
    }

    public boolean hasPermissions(@NotNull String[] ids) {
        List<VirtualModelEntity> results = virtualModelRepository.findAllByTenantIdAndVirtualModelIdIn(
            SecurityUtils.getCurrentUser().tenantId(),
            Arrays.stream(ids).map(UUID::fromString).toList());
        return results.size() == ids.length;
    }
}
