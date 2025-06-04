package chat.gptalk.common.entity;


import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "llm_providers")
@Builder
public record LlmProviderEntity(
    @Id
    Integer id,
    UUID userId,
    UUID tenantId,
    UUID providerId,
    String name,
    String baseUrl,
    String description,
    String sdkClientClass,
    Boolean system,
    Boolean enabled,
    Map<String, Object> extraConfig,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {

}

