package chat.gptalk.connector.security;

import chat.gptalk.security.security.SecurityConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ApiKeyInjectionFilter implements ExchangeFilterFunction {

    private final SpApiKeyManager apiKeyManager;

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return Mono.deferContextual(contextView -> apiKeyManager.getSpKey(contextView.get("model"))
            .flatMap(key -> {
                ClientRequest mutate = ClientRequest.from(request)
                    .header(HttpHeaders.AUTHORIZATION, SecurityConstants.BEARER_PREFIX + key.getValue())
                    .build();
                return next.exchange(mutate);
            }));
    }
}
