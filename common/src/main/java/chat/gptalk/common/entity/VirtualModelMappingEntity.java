package chat.gptalk.common.entity;

import java.util.UUID;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "virtual_model_mappings")
@Builder
public record VirtualModelMappingEntity(
    @Id
    Integer id,
    UUID userId,
    UUID tenantId,
    UUID virtualModelId,
    UUID modelId,
    Integer priority,
    Integer weight
) {

}

