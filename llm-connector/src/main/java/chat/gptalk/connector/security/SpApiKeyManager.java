package chat.gptalk.connector.security;

import reactor.core.publisher.Mono;

public interface SpApiKeyManager {

    Mono<SpApiKey> getSpKey(String model);
}
