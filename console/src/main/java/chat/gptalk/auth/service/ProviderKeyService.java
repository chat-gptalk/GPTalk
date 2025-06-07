package chat.gptalk.auth.service;

import chat.gptalk.auth.model.request.CreateProviderKeyRequest;
import chat.gptalk.auth.model.request.PatchProviderKeyRequest;
import chat.gptalk.auth.model.response.ProviderKeyResponse;
import chat.gptalk.auth.model.response.ProviderKeyValueResponse;
import chat.gptalk.auth.repository.ProviderKeyRepository;
import chat.gptalk.auth.util.SecurityUtils;
import chat.gptalk.common.crypto.CryptoProvider;
import chat.gptalk.common.entity.LlmProviderKeyEntity;
import chat.gptalk.common.exception.DataConflictException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProviderKeyService {

    private final CryptoProvider cryptoProvider;
    private final ProviderService providerService;
    private final ProviderKeyRepository providerKeyRepository;

    public List<ProviderKeyResponse> getProviderKeys(String providerId) {
        return providerKeyRepository.findByTenantId(SecurityUtils.getTenantId(), Sort.by(Order.desc("id")))
            .stream()
            .filter(it -> providerId == null || it.providerId().toString().equals(providerId))
            .map(this::mapToResponse)
            .toList();
    }

    private ProviderKeyResponse mapToResponse(LlmProviderKeyEntity entity) {
        return ProviderKeyResponse.builder()
            .providerKeyId(entity.providerKeyId())
            .enabled(entity.enabled())
            .name(entity.name())
            .description(entity.description())
            .provider(providerService.findByProviderId(entity.providerId()))
            .priority(entity.priority())
            .createdAt(entity.createdAt())
            .updatedAt(entity.updatedAt())
            .build();
    }

    public ProviderKeyResponse createProviderKey(@Valid CreateProviderKeyRequest createRequest) {
        if (providerKeyRepository.existsByTenantIdAndName(SecurityUtils.getTenantId(), createRequest.name())) {
            throw new DataConflictException("The provider already exists");
        }
        LlmProviderKeyEntity providerKeyEntity = LlmProviderKeyEntity.builder()
            .providerKeyId(UUID.randomUUID())
            .providerId(UUID.fromString(createRequest.providerId()))
            .name(createRequest.name())
            .enabled(createRequest.enabled())
            .priority(createRequest.priority())
            .description(createRequest.description())
            .keyEnc(cryptoProvider.encrypt(createRequest.key()))
            .userId(SecurityUtils.getCurrentUser().userId())
            .tenantId(SecurityUtils.getTenantId())
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build();
        providerKeyRepository.save(providerKeyEntity);
        return mapToResponse(providerKeyEntity);
    }

    @Transactional
    public void batchDelete(@NotNull String[] ids) {
        providerKeyRepository.deleteByTenantIdAndProviderKeyIdIn(
            SecurityUtils.getTenantId(), Arrays.stream(ids).map(UUID::fromString).toList());
    }

    public ProviderKeyResponse patchProviderKey(String providerKeyId,
        @Valid PatchProviderKeyRequest patchRequest) {
        LlmProviderKeyEntity providerKeyEntity = providerKeyRepository.findOneByTenantIdAndProviderKeyId(
            SecurityUtils.getTenantId(), UUID.fromString(providerKeyId));
        if (patchRequest.enabled() != null) {
            providerKeyEntity = providerKeyEntity.withEnabled(patchRequest.enabled());
        }
        if (patchRequest.name() != null) {
            boolean exists = providerKeyRepository.existsByTenantIdAndIdNotAndName(SecurityUtils.getTenantId(),
                providerKeyEntity.id(), patchRequest.name());
            if (exists) {
                throw new DataConflictException("The providerKey name already exists");
            }
            providerKeyEntity = providerKeyEntity.withName(patchRequest.name());
        }
        if (patchRequest.key() != null) {
            providerKeyEntity = providerKeyEntity.withKeyEnc(cryptoProvider.encrypt(patchRequest.key()));
        }
        if (patchRequest.priority() != null) {
            providerKeyEntity = providerKeyEntity.withPriority(patchRequest.priority());
        }
        if (patchRequest.description() != null) {
            providerKeyEntity = providerKeyEntity.withDescription(patchRequest.description());
        }
        providerKeyRepository.save(providerKeyEntity);
        return mapToResponse(providerKeyEntity);
    }

    public ProviderKeyValueResponse getProviderKeyValue(String providerKeyId) {
        LlmProviderKeyEntity entity = providerKeyRepository.findOneByTenantIdAndProviderKeyId(
            SecurityUtils.getTenantId(), UUID.fromString(providerKeyId));
        return new ProviderKeyValueResponse(cryptoProvider.decrypt(entity.keyEnc()));
    }
}
