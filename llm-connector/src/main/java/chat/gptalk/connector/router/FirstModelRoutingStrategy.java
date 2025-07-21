package chat.gptalk.connector.router;

import chat.gptalk.connector.model.LlmModel;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class FirstModelRoutingStrategy implements ModelRoutingStrategy {

    @Override
    public Mono<LlmModel> select(UUID tenantId, List<LlmModel> candidates) {
        return Mono.just(candidates.get(0));
    }
}
