package chat.gptalk.connector.router;

import chat.gptalk.connector.model.ProviderModel;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Mono;


public interface ModelRoutingStrategy {

    Mono<ProviderModel> select(UUID tenantId, List<ProviderModel> candidates);
}
