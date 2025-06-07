package chat.gptalk.common.entity;


import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "llm_provider_keys")
@Builder
@With
public record LlmProviderKeyEntity(
    @Id
    Integer id,
    UUID userId,
    UUID tenantId,
    UUID providerKeyId,
    UUID providerId,
    String keyEnc,
    String name,
    String description,
    Integer priority,
    Boolean enabled,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {

}
