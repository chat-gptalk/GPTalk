package chat.gptalk.common.entity;


import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "llm_model_pricing")
public record LlmModelPricingEntity(
    @Id
    Integer id,
    UUID userId,
    UUID tenantId,
    UUID modelId,
    String modality,
    String usageUnit,
    BigDecimal pricePerUnit,
    String currency,
    OffsetDateTime effectiveFrom
) {}

