package chat.gptalk.common.model.request;

import jakarta.validation.constraints.NotNull;

public record BatchDeleteRequest(
    @NotNull String[] ids
) {
}