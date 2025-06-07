package chat.gptalk.auth.controller.user;

import chat.gptalk.common.constants.ModelFeature;
import chat.gptalk.common.constants.ModelStatus;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/config")
@RequiredArgsConstructor
public class ConfigController {

    @GetMapping("model/features")
    public List<ModelFeature> getModelFeatures() {
        return Arrays.stream(ModelFeature.values()).toList();
    }

    @GetMapping("model/status")
    public List<ModelStatus> getModelStatus() {
        return Arrays.stream(ModelStatus.values()).toList();
    }

}
