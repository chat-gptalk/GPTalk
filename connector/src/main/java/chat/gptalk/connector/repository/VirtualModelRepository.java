package chat.gptalk.connector.repository;

import chat.gptalk.common.entity.LlmModelEntity;
import chat.gptalk.common.entity.VirtualModelEntity;
import java.util.List;
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
public class VirtualModelRepository {

    private final DatabaseClient databaseClient;
    private final R2dbcEntityOperations entityOperations;

    public Mono<VirtualModelEntity> findByName(UUID tenantId, String modelName) {
        return entityOperations.selectOne(Query.query(Criteria
            .where("name").is(modelName)
            .and("tenant_id").is(tenantId)
            .and("enabled").is(true)
        ), VirtualModelEntity.class);
    }

    public Mono<List<LlmModelEntity>> findModelsByVirtualModelId(UUID tenantId, UUID virtualModelId) {
        String sql = """
            select lm.* from llm_models lm
            join virtual_model_mappings vm on lm.model_id = vm.model_id
            where vm.virtual_model_id=:virtualModelId and vm.tenant_id = :tenantId
            """;
        return databaseClient.sql(sql)
            .bind("virtualModelId", virtualModelId)
            .bind("tenantId", tenantId)
            .mapProperties(LlmModelEntity.class)
            .all()
            .collectList()
            ;
    }
}
