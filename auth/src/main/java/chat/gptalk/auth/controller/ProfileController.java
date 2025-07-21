package chat.gptalk.auth.controller;

import chat.gptalk.auth.util.SecurityUtils;
import chat.gptalk.security.model.AdminUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class ProfileController {

    @GetMapping("profile")
    public AdminUser profile() {
        return SecurityUtils.getCurrentUser();
    }
}
