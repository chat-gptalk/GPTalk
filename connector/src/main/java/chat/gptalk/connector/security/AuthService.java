package chat.gptalk.connector.security;

import chat.gptalk.connector.repository.ApiKeyRepository;
import chat.gptalk.security.util.ApiKeyUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ApiKeyRepository apiKeyRepository;

    public Mono<OpenApiUser> verify(String key) {
        return apiKeyRepository.findByKey(ApiKeyUtils.hash(key))
            .map(it -> OpenApiUser.builder()
                .userId(it.userId())
                .apiKeyId(it.virtualKeyId())
                .tenantId(it.tenantId())
                .build())
            .switchIfEmpty(Mono.error(new BadCredentialsException("Invalid key")));
    }

    public Mono<Boolean> verifyApiKeyOwnership(UUID tenantId, UUID virtualKeyId) {
        return apiKeyRepository.existsByTenantIdAndVirtualKeyId(tenantId, virtualKeyId);
    }
}
