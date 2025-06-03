package chat.gptalk.common.entity;


import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "api_keys")
@Builder
public record ApiKeyEntity(
    @Id
    Integer id,
    UUID userId,
    UUID tenantId,
    UUID apiKeyId,
    String keyHash,
    String name,
    List<String> allowedModels,
    Integer status,
    OffsetDateTime expireAt,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {

}

