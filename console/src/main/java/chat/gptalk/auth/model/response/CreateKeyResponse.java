package chat.gptalk.auth.model.response;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.With;

@With
public record CreateKeyResponse(
    @NotNull UUID apiKeyId,
    @NotNull String key,
    @NotNull String name,
    @NotNull UUID userId,
    @NotNull UUID tenantId,
    List<String> allowedModels,
    @NotNull Integer status,
    OffsetDateTime expireAt,
    @NotNull OffsetDateTime createdAt,
    @NotNull OffsetDateTime updatedAt
) {

}
