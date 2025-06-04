package chat.gptalk.auth.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.Map;

public record CreateProviderRequest(
    @NotBlank String name,
    @NotBlank @Pattern(regexp = "^(https?://)?([a-zA-Z0-9.-]+)(:\\d+)?(/\\S*)?$") String baseUrl,
    String description,
    @NotBlank String sdkClientClass,
    Map<String, Object> extraConfig
) {

}
