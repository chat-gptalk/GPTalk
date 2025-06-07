package chat.gptalk.auth.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateProviderKeyRequest(
    @NotBlank String name,
    @NotBlank String providerId,
    @NotNull @Min(1) @Max(100) Integer priority,
    @NotBlank String key,
    String description,
    @NotNull Boolean enabled
) {

}
