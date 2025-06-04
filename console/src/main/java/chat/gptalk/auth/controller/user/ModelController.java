package chat.gptalk.auth.controller.user;

import chat.gptalk.common.constants.ModelFeature;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/models")
@RequiredArgsConstructor
public class ModelController {

    @GetMapping("features")
    public List<ModelFeature> getModelFeatures() {
        return Arrays.stream(ModelFeature.values()).toList();
    }
}
