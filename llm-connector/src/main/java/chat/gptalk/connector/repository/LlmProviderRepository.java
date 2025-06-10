package chat.gptalk.connector.repository;

import chat.gptalk.common.entity.LlmProviderEntity;
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
public class LlmProviderRepository {

    private final DatabaseClient databaseClient;
    private final R2dbcEntityOperations entityOperations;

    public Mono<LlmProviderEntity> findByProviderId(UUID providerId) {
        return entityOperations.selectOne(Query.query(Criteria
                .where("provider_id").is(providerId)
            ), LlmProviderEntity.class);
    }
}
