package chat.gptalk.gateway.repository;

import chat.gptalk.common.constants.EntityStatus;
import chat.gptalk.common.entity.VirtualKeyEntity;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class ApiKeyRepository {

    private final DatabaseClient databaseClient;
    private final R2dbcEntityOperations entityOperations;

    public Mono<VirtualKeyEntity> findByKey(String keyHash) {
        return entityOperations.selectOne(Query.query(Criteria
            .where("key_hash").is(keyHash)
            .and("status").is(EntityStatus.ACTIVE.getCode())
        ), VirtualKeyEntity.class);
    }

    public Mono<Boolean> existsByTenantIdAndVirtualKeyId(UUID tenantId, UUID virtualKeyId) {
        return entityOperations.exists(Query.query(Criteria
            .where("tenant_id").is(tenantId)
            .and("virtual_key_id").is(virtualKeyId)
            .and("status").is(EntityStatus.ACTIVE.getCode())
        ), VirtualKeyEntity.class);
    }
}
