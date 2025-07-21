package chat.gptalk.admin.model.request;

import jakarta.validation.constraints.Pattern;
import java.util.List;
import java.util.UUID;

public record PatchVirtualModelRequest(
   @Pattern(regexp = "^vm:[a-z][a-z0-9-]{0,49}$") String name,
    String description,
    List<UUID> modelIds
) {

}
