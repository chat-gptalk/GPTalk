package chat.gptalk.auth.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PatchProviderKeyRequest(
    String name,
    String key,
    @NotNull @Min(1) @Max(100) Integer priority,
    String description,
    Boolean enabled

) {

}
