package chat.gptalk.auth.service;

import chat.gptalk.auth.model.request.CreateVirtualKeyRequest;
import chat.gptalk.auth.model.response.VirtualKeyResponse;
import chat.gptalk.auth.repository.VirtualKeyRepository;
import chat.gptalk.auth.util.SecurityUtils;
import chat.gptalk.common.constants.EntityStatus;
import chat.gptalk.common.entity.VirtualKeyEntity;
import chat.gptalk.common.security.ConsoleAuthenticatedUser;
import chat.gptalk.common.util.ApiKeyUtils;
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
public class VirtualKeyService {

    private final VirtualKeyRepository virtualKeyRepository;

    public List<VirtualKeyResponse> getKeys() {
        return virtualKeyRepository.findAll(Sort.by(Order.desc("id"))).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    public VirtualKeyResponse createKey(CreateVirtualKeyRequest createKeyRequest) {
        ConsoleAuthenticatedUser currentUser = SecurityUtils.getCurrentUser();
        String key = ApiKeyUtils.newKey();
        VirtualKeyEntity entity = VirtualKeyEntity.builder()
            .virtualKeyId(UUID.randomUUID())
            .status(EntityStatus.ACTIVE.getCode())
            .name(createKeyRequest.name())
            .keyHash(ApiKeyUtils.hash(key))
            .userId(currentUser.userId())
            .tenantId(currentUser.tenantId())
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build();
        entity = virtualKeyRepository.save(entity);
        return mapToResponse(entity)
            .withKey(key);
    }

    private VirtualKeyResponse mapToResponse(VirtualKeyEntity entity) {
        return VirtualKeyResponse
            .builder()
            .virtualKeyId(entity.virtualKeyId())
            .name(entity.name())
            .status(entity.status())
            .allowedModels(entity.allowedModels())
            .expireAt(entity.expireAt())
            .createdAt(entity.createdAt())
            .updatedAt(entity.updatedAt())
            .build();
    }

    @Transactional
    public void batchDelete(@NotNull List<UUID> ids) {
        virtualKeyRepository.deleteByTenantIdAndVirtualKeyIdIn(SecurityUtils.getTenantId(),
            ids);
    }
}
