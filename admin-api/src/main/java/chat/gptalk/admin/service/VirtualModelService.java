package chat.gptalk.admin.service;

import chat.gptalk.admin.model.request.CreateVirtualModelRequest;
import chat.gptalk.admin.model.request.PatchVirtualModelRequest;
import chat.gptalk.admin.model.response.ModelResponse;
import chat.gptalk.admin.model.response.VirtualModelResponse;
import chat.gptalk.admin.repository.VirtualModelMappingRepository;
import chat.gptalk.admin.repository.VirtualModelRepository;
import chat.gptalk.common.constants.ErrorCode;
import chat.gptalk.common.entity.VirtualModelEntity;
import chat.gptalk.common.entity.VirtualModelMappingEntity;
import chat.gptalk.common.exception.BizException;
import chat.gptalk.security.model.AuthUser;
import chat.gptalk.security.util.SecurityUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
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
        if (virtualModelRepository.existsByTenantIdAndName(SecurityUtils.getTenantId(), name)) {
            throw new BizException(ErrorCode.ADM_CONFLICT, createVirtualModelRequest.name());
        }
        UUID virtualModelId = UUID.randomUUID();
        VirtualModelEntity virtualModelEntity = VirtualModelEntity.builder()
            .name(name)
            .description(createVirtualModelRequest.description())
            .virtualModelId(virtualModelId)
            .enabled(true)
            .userId(SecurityUtils.getCurrentUser(AuthUser.class).userId())
            .tenantId(SecurityUtils.getTenantId())
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build();
        virtualModelEntity = virtualModelRepository.save(virtualModelEntity);
        List<VirtualModelMappingEntity> mappings = createVirtualModelRequest.modelIds().stream()
            .map(modelId -> VirtualModelMappingEntity.builder()
                .modelId(modelId)
                .virtualModelId(virtualModelId)
                .userId(SecurityUtils.getCurrentUser(AuthUser.class).userId())
                .tenantId(SecurityUtils.getTenantId())
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
            .description(entity.description())
            .createdAt(entity.createdAt())
            .updatedAt(entity.updatedAt())
            .build();
    }

    @Transactional
    public void batchDelete(@NotNull List<UUID> ids) {
        virtualModelRepository.deleteByTenantIdAndVirtualModelIdIn(SecurityUtils.getTenantId(), ids);
    }

    @Transactional
    public VirtualModelResponse patchVirtualModel(UUID virtualModelId,
        @Valid PatchVirtualModelRequest patchVirtualModelRequest) {
        VirtualModelEntity entity = virtualModelRepository.findAllByTenantIdAndVirtualModelId(
            SecurityUtils.getTenantId(), virtualModelId);
        if (patchVirtualModelRequest.name() != null) {
            entity = entity.withName(patchVirtualModelRequest.name());
        }
        if (patchVirtualModelRequest.description() != null) {
            entity = entity.withDescription(patchVirtualModelRequest.description());
        }
        if (patchVirtualModelRequest.modelIds() != null) {
            boolean hasPermissions = modelService.hasPermissions(patchVirtualModelRequest.modelIds());
            if (!hasPermissions) {
                throw new BizException(ErrorCode.AU_FORBIDDEN);
            }
            virtualModelMappingRepository.deleteByModelId(virtualModelId);
            List<VirtualModelMappingEntity> mappings = patchVirtualModelRequest.modelIds().stream()
                .map(modelId -> VirtualModelMappingEntity.builder()
                    .modelId(modelId)
                    .virtualModelId(virtualModelId)
                    .userId(SecurityUtils.getCurrentUser(AuthUser.class).userId())
                    .tenantId(SecurityUtils.getTenantId())
                    .weight(0)
                    .priority(0)
                    .build())
                .toList();
            virtualModelMappingRepository.saveAll(mappings);
        }
        virtualModelRepository.save(entity);
        return mapToResponse(entity);
    }
}
