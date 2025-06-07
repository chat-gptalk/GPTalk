package chat.gptalk.common.entity;


import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "virtual_models")
@Builder
@With
public record VirtualModelEntity(
    @Id
    Integer id,
    UUID userId,
    UUID tenantId,
    String name,
    String description,
    UUID virtualModelId,
    Boolean enabled,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {

}

