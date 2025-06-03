package chat.gptalk.auth.model.request;

import jakarta.validation.constraints.Pattern;
import java.util.List;

public record CreateVirtualModelRequest(
   @Pattern(regexp = "^[a-z][a-z0-9-]{0,49}$") String name,
    String description,
    List<String> modelIds
) {

}
