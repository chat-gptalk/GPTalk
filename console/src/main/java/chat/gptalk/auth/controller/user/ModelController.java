package chat.gptalk.auth.controller.user;

import chat.gptalk.auth.model.request.CreateModelRequest;
import chat.gptalk.auth.model.request.PatchModelRequest;
import chat.gptalk.auth.model.response.ModelResponse;
import chat.gptalk.auth.service.ModelService;
import chat.gptalk.common.constants.ModelFeature;
import chat.gptalk.common.model.request.BatchDeleteRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.Arrays;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/models")
@RequiredArgsConstructor
public class ModelController {

    private final ModelService modelService;

    @GetMapping("features")
    public List<ModelFeature> getModelFeatures() {
        return Arrays.stream(ModelFeature.values()).toList();
    }

    @GetMapping
    public List<ModelResponse> getModels() {
        return modelService.getModels();
    }

    @PostMapping
    @PreAuthorize("@providerService.hasPermission(#createRequest.providerId())")
    public ModelResponse createProviderModel(@RequestBody @Valid CreateModelRequest createRequest) {
        return modelService.createModel(createRequest);
    }

    @PatchMapping("{modelId}")
    public ModelResponse patchProviderModel(
        @PathVariable("modelId") String modelId,
        @RequestBody @Valid PatchModelRequest patchRequest) {
        return modelService.patchProviderModel(modelId, patchRequest);
    }

    @Operation(operationId = "batchDeleteProviderModel")
    @DeleteMapping
    public void batchDeleteProviderModel(@RequestBody BatchDeleteRequest batchDeleteRequest) {
        modelService.batchDelete(batchDeleteRequest.ids());
    }
}
