package chat.gptalk.common.security;

import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ConsoleAuthenticatedUser(
    UUID userId,
    String username,
    UUID tenantId,
    List<String> roles
) {
}