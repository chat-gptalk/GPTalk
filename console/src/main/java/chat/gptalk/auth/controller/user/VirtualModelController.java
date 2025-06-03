package chat.gptalk.auth.controller.user;

import chat.gptalk.auth.model.request.CreateVirtualModelRequest;
import chat.gptalk.auth.model.response.VirtualModelResponse;
import chat.gptalk.auth.service.VirtualModelService;
import chat.gptalk.common.model.request.BatchDeleteRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
    public List<VirtualModelResponse> getModels() {
        return virtualModelService.getModels();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VirtualModelResponse createModel(@Valid @RequestBody CreateVirtualModelRequest createVirtualModelRequest) {
        return virtualModelService.createModel(createVirtualModelRequest);
    }

    @DeleteMapping
    @PreAuthorize("@virtualModelService.hasPermissions(#batchDeleteRequest.ids())")
    public void batchDelete(@RequestBody BatchDeleteRequest batchDeleteRequest) {
        virtualModelService.batchDelete(batchDeleteRequest.ids());
    }
}
