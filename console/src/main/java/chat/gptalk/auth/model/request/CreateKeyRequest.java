package chat.gptalk.auth.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateKeyRequest(
    @NotBlank @Size(min = 2, max = 50) String name
) {

}
