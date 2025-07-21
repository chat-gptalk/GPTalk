package chat.gptalk.connector.router;

import chat.gptalk.connector.model.LlmModel;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Mono;


public interface ModelRoutingStrategy {

    Mono<LlmModel> select(UUID tenantId, List<LlmModel> candidates);
}
