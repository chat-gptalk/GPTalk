package chat.gptalk.common.entity;


import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "tenants")
@Builder
public record TenantEntity(
    @Id
    Integer id,
    UUID tenantId,
    String name,
    UUID ownerUserId,
    Integer status,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}

