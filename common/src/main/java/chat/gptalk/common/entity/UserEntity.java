package chat.gptalk.common.entity;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "users")
@Builder
public record UserEntity(
    @Id
    Integer id,
    UUID userId,
    UUID tenantId,
    String username,
    String password,
    List<String> roles,
    Integer status,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {

}
