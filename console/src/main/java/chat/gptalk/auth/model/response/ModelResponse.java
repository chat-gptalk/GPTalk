package chat.gptalk.auth.model.response;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.With;

@With
@Builder
public record ModelResponse(
    @NotNull UUID modelId,
    @NotNull ProviderResponse provider,
    @NotNull String name,
    @NotNull List<String> features,
    @NotNull Integer contextLength,
    @NotNull Integer maxOutputTokens,
    @NotNull Boolean enabled,
    Map<String, Object> defaultParams,
    @NotNull OffsetDateTime createdAt,
    @NotNull OffsetDateTime updatedAt
) {

}
