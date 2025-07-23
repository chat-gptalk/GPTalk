package chat.gptalk.connector.model;

import lombok.Builder;

@Builder
public record ModelMeta(
    LlmModel model,
    LlmProvider provider,
    VirtualModel virtualModel
) {

}
