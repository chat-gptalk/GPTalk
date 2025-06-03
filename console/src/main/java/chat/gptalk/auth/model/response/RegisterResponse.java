package chat.gptalk.auth.model.response;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.Builder;

@Builder
public record RegisterResponse(
    @NotNull String username,
    @NotNull String userId,
    @NotNull String password,
    @NotNull Integer status,
    @NotNull String tenantId,
    @NotNull OffsetDateTime createdAt,
    @NotNull OffsetDateTime updatedAt) {

}
