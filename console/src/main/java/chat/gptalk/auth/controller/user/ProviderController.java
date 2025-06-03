package chat.gptalk.auth.controller.user;

import chat.gptalk.auth.model.response.ModelResponse;
import chat.gptalk.auth.model.response.ProviderResponse;
import chat.gptalk.auth.model.response.TreeNode;
import chat.gptalk.auth.service.ModelService;
import chat.gptalk.auth.service.ProviderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/providers")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;
    private final ModelService modelService;

    @GetMapping
    public List<ProviderResponse> getProviders() {
        return providerService.getCurrentTenantProviders();
    }

    @GetMapping("tree")
    public List<TreeNode> getProviderTree() {
        return providerService.getProviderTree();
    }

    @GetMapping("{providerId}/models")
    public List<ModelResponse> getProviderModels(@PathVariable("providerId") String providerId) {
        return modelService.getProviderModels(providerId);
    }
}
