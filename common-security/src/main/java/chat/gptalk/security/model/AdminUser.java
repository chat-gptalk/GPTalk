package chat.gptalk.security.model;

import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record AdminUser(
    UUID userId,
    String username,
    UUID tenantId,
    List<String> roles
) {
}