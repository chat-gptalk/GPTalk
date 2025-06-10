package chat.gptalk.connector.router;

import chat.gptalk.connector.model.ProviderModel;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class FirstModelRoutingStrategy implements ModelRoutingStrategy {

    @Override
    public Mono<ProviderModel> select(UUID tenantId, List<ProviderModel> candidates) {
        return Mono.just(candidates.get(0));
    }
}
