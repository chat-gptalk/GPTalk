package chat.gptalk.auth.controller.user;

import chat.gptalk.auth.model.request.CreateKeyRequest;
import chat.gptalk.auth.model.response.CreateKeyResponse;
import chat.gptalk.auth.service.ApiKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @PostMapping
    public CreateKeyResponse createKey(@Valid @RequestBody CreateKeyRequest createKeyRequest) {
        return apiKeyService.createKey(createKeyRequest);
    }
}
