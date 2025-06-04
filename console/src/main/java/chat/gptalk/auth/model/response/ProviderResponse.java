package chat.gptalk.auth.model.response;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ProviderResponse(
    @NotNull UUID providerId,
    @NotNull String name,
    @NotNull String baseUrl,
    String description,
    @NotNull String sdkClientClass,
    @NotNull Boolean system,
    @NotNull Boolean enabled,
    Map<String, Object> extraConfig,
    @NotNull Integer modelCount,
    @NotNull OffsetDateTime createdAt,
    @NotNull OffsetDateTime updatedAt
) {

}
