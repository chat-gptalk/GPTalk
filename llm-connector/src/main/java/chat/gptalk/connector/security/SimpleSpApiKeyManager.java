package chat.gptalk.connector.security;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class SimpleSpApiKeyManager implements SpApiKeyManager {

    @Override
    public Mono<SpApiKey> getSpKey(String model) {
        return Mono.just(new SpApiKey() {
            @Override
            public String getValue() {
                return System.getenv("OPENAI_API_KEY");
            }
        });
    }
}
