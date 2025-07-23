package chat.gptalk.connector.security;

import chat.gptalk.security.model.AuthenticatedUser;
import java.util.UUID;
import lombok.Builder;

@Builder
public record OpenApiUser(
    UUID userId,
    UUID apiKeyId,
    UUID tenantId
) implements AuthenticatedUser {

}