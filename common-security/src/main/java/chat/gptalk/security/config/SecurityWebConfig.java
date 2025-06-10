package chat.gptalk.security.config;

import chat.gptalk.common.security.ApiAuthenticatedUser;
import chat.gptalk.common.security.SecurityConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
public class SecurityWebConfig {

    @Bean
    public WebClient securityWebClient() {
        return WebClient.builder()
            .filter(userInfoHeaderFilter())
            .build();
    }

    private ExchangeFilterFunction userInfoHeaderFilter() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> ReactiveSecurityContextHolder.getContext()
            .map(it -> {
                ApiAuthenticatedUser user = (ApiAuthenticatedUser) it.getAuthentication().getPrincipal();
                HttpHeaders headers = clientRequest.headers();
                headers.set(SecurityConstants.HEADER_TENANT_ID, user.tenantId().toString());
                headers.set(SecurityConstants.HEADER_USER_ID, user.userId().toString());
                headers.set(SecurityConstants.HEADER_API_KEY_ID, user.apiKeyId().toString());
                return clientRequest;
            }));
    }
}
