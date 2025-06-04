package chat.gptalk.auth.controller.user;

import chat.gptalk.auth.model.request.CreateProviderModelRequest;
import chat.gptalk.auth.model.request.CreateProviderRequest;
import chat.gptalk.auth.model.response.ModelResponse;
import chat.gptalk.auth.model.response.ProviderResponse;
import chat.gptalk.auth.model.response.TreeNode;
import chat.gptalk.auth.service.ModelService;
import chat.gptalk.auth.service.ProviderService;
import chat.gptalk.common.model.request.BatchDeleteRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/providers")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;
    private final ModelService modelService;

    @PostMapping
    public ProviderResponse createProvider(@RequestBody @Valid CreateProviderRequest createProviderRequest) {
        return providerService.createProvider(createProviderRequest);
    }

    @GetMapping
    public List<ProviderResponse> getProviders() {
        return providerService.getCurrentTenantProviders();
    }

    @GetMapping("tree")
    public List<TreeNode> getProviderTree() {
        return providerService.getProviderTree();
    }

    @GetMapping("{providerId}")
    public ProviderResponse getProvider(@PathVariable("providerId") String providerId) {
        return providerService.getProvider(providerId);
    }

    @Operation(operationId = "batchDeleteProvider")
    @DeleteMapping
    @PreAuthorize("@providerService.hasPermissions(#batchDeleteRequest.ids())")
    public void batchDelete(@RequestBody BatchDeleteRequest batchDeleteRequest) {
        providerService.batchDelete(batchDeleteRequest.ids());
    }

    @GetMapping("{providerId}/models")
    public List<ModelResponse> getProviderModels(@PathVariable("providerId") String providerId) {
        return modelService.getProviderModels(providerId);
    }

    @PostMapping("{providerId}/models")
    @PreAuthorize("@providerService.hasPermission(#providerId)")
    public ModelResponse createProviderModel(@PathVariable("providerId") String providerId,
        @RequestBody @Valid CreateProviderModelRequest createRequest) {
        return modelService.createProviderModel(providerId, createRequest);
    }

    @Operation(operationId = "batchDeleteProviderModel")
    @DeleteMapping("{providerId}/models")
    @PreAuthorize("@providerService.hasPermission(#providerId)")
    public void batchDeleteProviderModel(@PathVariable String providerId,
        @RequestBody BatchDeleteRequest batchDeleteRequest) {
        modelService.batchDelete(providerId, batchDeleteRequest.ids());
    }
}
