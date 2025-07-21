package chat.gptalk.auth.model.response;

import jakarta.validation.constraints.NotNull;
import lombok.With;

@With
public record LoginResponse(
    @NotNull String accessToken,
    @NotNull String refreshToken,
    @NotNull Long expiresIn,
    @NotNull String tokenType
) {

}
