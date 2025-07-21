package chat.gptalk.security.model;

import java.util.UUID;
import lombok.Builder;

@Builder
public record OpenApiUser(
    UUID userId,
    UUID apiKeyId,
    UUID tenantId
) {
}