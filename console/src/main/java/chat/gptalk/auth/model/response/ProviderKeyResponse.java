package chat.gptalk.auth.model.response;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.With;

@With
@Builder
public record ProviderKeyResponse(
    @NotNull UUID providerKeyId,
    @NotNull ProviderResponse provider,
    @NotNull String name,
    String description,
    @NotNull Integer priority,
    @NotNull Boolean enabled,
    @NotNull OffsetDateTime createdAt,
    @NotNull OffsetDateTime updatedAt
) {

}
