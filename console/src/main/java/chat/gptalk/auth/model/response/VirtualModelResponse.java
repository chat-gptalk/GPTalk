package chat.gptalk.auth.model.response;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record VirtualModelResponse(
    @NotNull String name,
    @NotNull UUID virtualModelId,
    @NotNull Boolean enabled,
    @NotNull List<MappedModelResponse> models,
    @NotNull OffsetDateTime createdAt,
    @NotNull OffsetDateTime updatedAt
) {

    @Builder
    public record MappedModelResponse(
        @NotNull ModelResponse model,
        @NotNull Integer priority,
        @NotNull Integer weight
    ) {

    }

}
