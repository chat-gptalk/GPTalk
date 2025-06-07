package chat.gptalk.auth.controller.user;

import chat.gptalk.auth.model.request.CreateModelRequest;
import chat.gptalk.auth.model.request.PatchModelRequest;
import chat.gptalk.auth.model.response.ModelResponse;
import chat.gptalk.auth.service.ModelService;
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
@RequestMapping("api/v1/models")
@RequiredArgsConstructor
public class ModelController {

    private final ModelService modelService;

    @GetMapping
    public List<ModelResponse> getModels(@RequestParam(required = false) String providerId, @RequestParam(required = false) String status) {
        return modelService.getModels(providerId, status);
    }

    @PostMapping
    @PreAuthorize("@providerService.hasPermission(#createRequest.providerId())")
    public ModelResponse createModel(@RequestBody @Valid CreateModelRequest createRequest) {
        return modelService.createModel(createRequest);
    }

    @PatchMapping("{modelId}")
    public ModelResponse patchModel(
        @PathVariable("modelId") String modelId,
        @RequestBody @Valid PatchModelRequest patchRequest) {
        return modelService.patchModel(modelId, patchRequest);
    }

    @DeleteMapping
    public void batchDeleteModel(@RequestBody @Valid BatchDeleteRequest batchDeleteRequest) {
        modelService.batchDelete(batchDeleteRequest.ids());
    }
}
