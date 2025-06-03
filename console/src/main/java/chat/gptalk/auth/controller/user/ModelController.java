package chat.gptalk.auth.controller.user;

import chat.gptalk.auth.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/models")
@RequiredArgsConstructor
public class ModelController {

    private final ModelService modelService;

}
