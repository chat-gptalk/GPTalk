package chat.gptalk.auth.service;

import chat.gptalk.auth.model.request.CreateKeyRequest;
import chat.gptalk.auth.model.response.CreateKeyResponse;
import chat.gptalk.auth.repository.ApiKeyRepository;
import chat.gptalk.auth.util.SecurityUtils;
import chat.gptalk.common.constants.EntityStatus;
import chat.gptalk.common.entity.ApiKeyEntity;
import chat.gptalk.common.security.ConsoleAuthenticatedUser;
import chat.gptalk.common.util.ApiKeyUtils;
import chat.gptalk.common.util.BeanMapperUtils;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateKeyResponse createKey(CreateKeyRequest createKeyRequest) {
        ConsoleAuthenticatedUser currentUser = SecurityUtils.getCurrentUser();
        String key = ApiKeyUtils.newKey();
        ApiKeyEntity apiKeyEntity = ApiKeyEntity.builder()
            .apiKeyId(UUID.randomUUID())
            .status(EntityStatus.ACTIVE.getCode())
            .name(createKeyRequest.name())
            .keyHash(ApiKeyUtils.hash(key))
            .userId(currentUser.userId())
            .tenantId(currentUser.tenantId())
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build();
        apiKeyEntity = apiKeyRepository.save(apiKeyEntity);
        CreateKeyResponse createKeyResponse = BeanMapperUtils.map(apiKeyEntity, CreateKeyResponse.class);
        createKeyResponse = createKeyResponse.withKey(key);
        return createKeyResponse;
    }
}
