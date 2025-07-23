package chat.gptalk.connector.repository;

import chat.gptalk.common.entity.LlmModelEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class LlmModelRepository {

    private final DatabaseClient databaseClient;
    private final R2dbcEntityOperations entityOperations;

    public Mono<List<LlmModelEntity>> findAll() {
        return entityOperations.select(Query.query(Criteria
                .where("enabled").is(true)
            ), LlmModelEntity.class)
            .collectList();
    }
}
