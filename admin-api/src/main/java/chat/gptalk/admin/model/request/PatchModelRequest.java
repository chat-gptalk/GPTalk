package chat.gptalk.admin.model.request;

import chat.gptalk.common.constants.ModelFeature;
import java.util.List;

public record PatchModelRequest(
    String name,
    List<ModelFeature> features,
    Boolean enabled
    ) {

}
