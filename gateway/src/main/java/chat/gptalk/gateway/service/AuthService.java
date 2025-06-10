package chat.gptalk.gateway.service;

import chat.gptalk.common.security.ApiAuthenticatedUser;
import chat.gptalk.common.util.ApiKeyUtils;
import chat.gptalk.gateway.repository.ApiKeyRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ApiKeyRepository apiKeyRepository;

    public Mono<ApiAuthenticatedUser> verify(String key) {
        return apiKeyRepository.findByKey(ApiKeyUtils.hash(key))
            .map(it -> ApiAuthenticatedUser.builder()
                .userId(it.userId())
                .apiKeyId(it.virtualKeyId())
                .tenantId(it.tenantId())
                .build())
            .switchIfEmpty(Mono.error(new BadCredentialsException("Invalid key")));
    }

    public Mono<Boolean> verify(UUID tenantId, UUID virtualKeyId) {
        return apiKeyRepository.existsByTenantIdAndVirtualKeyId(tenantId, virtualKeyId);
    }
}
