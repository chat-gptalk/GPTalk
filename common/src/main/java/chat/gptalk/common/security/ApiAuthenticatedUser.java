package chat.gptalk.common.security;

import java.util.UUID;
import lombok.Builder;

@Builder
public record ApiAuthenticatedUser(
    UUID userId,
    UUID apiKeyId,
    UUID tenantId
) {
}