package chat.gptalk.auth.model.request;

import chat.gptalk.common.constants.ModelFeature;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record CreateModelRequest(
    @NotBlank String name,
    @NotBlank String providerId,
    @NotEmpty List<ModelFeature> features
    ) {

}
