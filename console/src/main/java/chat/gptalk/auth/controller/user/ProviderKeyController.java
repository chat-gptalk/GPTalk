package chat.gptalk.auth.controller.user;

import chat.gptalk.auth.model.request.CreateProviderKeyRequest;
import chat.gptalk.auth.model.request.PatchProviderKeyRequest;
import chat.gptalk.auth.model.response.ProviderKeyResponse;
import chat.gptalk.auth.model.response.ProviderKeyValueResponse;
import chat.gptalk.auth.service.ProviderKeyService;
import chat.gptalk.common.model.request.BatchDeleteRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/provider/keys")
@RequiredArgsConstructor
public class ProviderKeyController {

    private final ProviderKeyService providerKeyService;

    @GetMapping
    public List<ProviderKeyResponse> getProviderKeys(@RequestParam(required = false) String providerId) {
        return providerKeyService.getProviderKeys(providerId);
    }

    @PostMapping
    @PreAuthorize("@providerService.hasPermission(#createRequest.providerId())")
    public ProviderKeyResponse createProviderKey(@RequestBody @Valid CreateProviderKeyRequest createRequest) {
        return providerKeyService.createProviderKey(createRequest);
    }

    @GetMapping("{providerKeyId}/reveal")
    public ProviderKeyValueResponse getProviderKeyValue(@PathVariable("providerKeyId") String providerKeyId) {
        return providerKeyService.getProviderKeyValue(providerKeyId);
    }

    @PatchMapping("{providerKeyId}")
    public ProviderKeyResponse patchProviderKey(
        @PathVariable("providerKeyId") String providerKeyId,
        @RequestBody @Valid PatchProviderKeyRequest patchRequest) {
        return providerKeyService.patchProviderKey(providerKeyId, patchRequest);
    }

    @DeleteMapping
    public void batchDeleteProviderKey(@RequestBody @Valid BatchDeleteRequest batchDeleteRequest) {
        providerKeyService.batchDelete(batchDeleteRequest.ids());
    }
}
