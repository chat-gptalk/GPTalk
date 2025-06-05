package chat.gptalk.common.entity;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "llm_models")
@With
@Builder
public record LlmModelEntity(
    @Id
    Integer id,
    UUID userId,
    UUID tenantId,
    UUID modelId,
    UUID providerId,
    String name,
    String status,
    List<String> features,
    Integer contextLength,
    Integer maxOutputTokens,
    Boolean enabled,
    Map<String, Object> defaultParams,
    OffsetDateTime lastCheckedAt,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}
