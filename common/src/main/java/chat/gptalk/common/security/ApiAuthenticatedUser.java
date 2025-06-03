package chat.gptalk.common.security;

import lombok.Builder;

@Builder
public record ApiAuthenticatedUser(
    String userId,
    String apiKeyId,
    String tenantId
) {
}