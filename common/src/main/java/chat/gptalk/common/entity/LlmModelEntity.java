package chat.gptalk.common.entity;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "llm_models")
public record LlmModelEntity(
    @Id
    Integer id,
    UUID userId,
    UUID tenantId,
    UUID modelId,
    UUID providerId,
    String name,
    List<String> features,
    Integer contextLength,
    Integer maxOutputTokens,
    Boolean enabled,
    Map<String, Object> defaultParams,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}
