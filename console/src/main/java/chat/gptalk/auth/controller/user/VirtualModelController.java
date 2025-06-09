package chat.gptalk.auth.controller.user;

import chat.gptalk.auth.model.request.CreateVirtualModelRequest;
import chat.gptalk.auth.model.request.PatchVirtualModelRequest;
import chat.gptalk.auth.model.response.VirtualModelResponse;
import chat.gptalk.auth.service.VirtualModelService;
import chat.gptalk.common.model.request.BatchDeleteRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/virtual-models")
@RequiredArgsConstructor
public class VirtualModelController {

    private final VirtualModelService virtualModelService;

    @GetMapping
    public List<VirtualModelResponse> getVirtualModels() {
        return virtualModelService.getModels();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VirtualModelResponse createVirtualModel(
        @Valid @RequestBody CreateVirtualModelRequest createVirtualModelRequest) {
        return virtualModelService.createModel(createVirtualModelRequest);
    }

    @PatchMapping("{virtualModelId}")
    public VirtualModelResponse patchVirtualModel(@PathVariable UUID virtualModelId,
        @Valid @RequestBody PatchVirtualModelRequest patchVirtualModelRequest) {
        return virtualModelService.patchVirtualModel(virtualModelId, patchVirtualModelRequest);
    }

    @DeleteMapping
    public void batchDeleteVirtualModel(@RequestBody BatchDeleteRequest batchDeleteRequest) {
        virtualModelService.batchDelete(batchDeleteRequest.ids());
    }
}
