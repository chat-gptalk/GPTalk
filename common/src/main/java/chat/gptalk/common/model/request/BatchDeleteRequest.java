package chat.gptalk.common.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record BatchDeleteRequest(
    @NotNull @NotEmpty List<UUID> ids
) {
}