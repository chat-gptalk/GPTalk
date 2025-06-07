package chat.gptalk.common.entity;


import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "virtual_keys")
@Builder
public record VirtualKeyEntity(
    @Id
    Integer id,
    UUID userId,
    UUID tenantId,
    UUID virtualKeyId,
    String keyHash,
    String name,
    List<String> allowedModels,
    Integer status,
    OffsetDateTime expireAt,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {

}

