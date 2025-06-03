package chat.gptalk.common.entity;


import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "rate_limits")
public record RateLimitEntity(
    @Id
    Long id,
    UUID userId,
    UUID tenantId,
    String targetType,
    UUID targetId,
    String windowType,
    String windowUnit,
    Integer windowSize,
    Integer rpm,
    Integer maxTokens,
    BigDecimal maxBudget,
    String description,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {

}

