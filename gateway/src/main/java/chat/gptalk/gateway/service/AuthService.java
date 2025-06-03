package chat.gptalk.gateway.service;

import chat.gptalk.common.security.ApiAuthenticatedUser;
import chat.gptalk.common.util.ApiKeyUtils;
import chat.gptalk.gateway.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
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
                .apiKeyId(it.keyId())
                .tenantId(it.tenantId())
                .build());
    }
}
