package chat.gptalk.common.entity;


import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "llm_keys")
public record LlmKeyEntity(
    @Id
    Integer id,
    UUID userId,
    UUID tenantId,
    UUID llmKeyId,
    UUID providerId,
    String llmKey,
    String name,
    Integer priority,
    Boolean active,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}
