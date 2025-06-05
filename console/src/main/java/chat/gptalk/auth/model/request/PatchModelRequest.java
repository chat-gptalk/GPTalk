package chat.gptalk.auth.model.request;

import chat.gptalk.common.constants.ModelFeature;
import java.util.List;

public record PatchModelRequest(
    String name,
    List<ModelFeature> features,
    Boolean enabled
    ) {

}
