package chat.gptalk.auth.model.response;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.With;

@With
@Builder
public record VirtualKeyResponse(
    @NotNull UUID virtualKeyId,
    @NotNull String key,
    @NotNull String name,
    List<String> allowedModels,
    @NotNull Integer status,
    OffsetDateTime expireAt,
    @NotNull OffsetDateTime createdAt,
    @NotNull OffsetDateTime updatedAt
) {

}
