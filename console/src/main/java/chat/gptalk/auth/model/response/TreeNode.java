package chat.gptalk.auth.model.response;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;

@Builder
public record TreeNode(
    @NotNull String title,
    @NotNull String value,
    @NotNull String key,
    @NotNull Boolean selectable,
    @NotNull List<TreeNode> children
) {

}
