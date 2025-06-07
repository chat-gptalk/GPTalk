package chat.gptalk.auth.model.response;

import jakarta.validation.constraints.NotNull;

public record ProviderKeyValueResponse(
    @NotNull String key
) {

}
