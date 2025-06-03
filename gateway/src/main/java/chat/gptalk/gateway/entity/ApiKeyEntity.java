package chat.gptalk.gateway.entity;

import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("api_keys")
public record ApiKeyEntity(
    @Id Long id,
    String keyId,
    String keyHash,
    String name,
    String userId,
    String tenantId,
    List<String> allowedModels,
    Integer status,
    OffsetDateTime expireAt,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}
