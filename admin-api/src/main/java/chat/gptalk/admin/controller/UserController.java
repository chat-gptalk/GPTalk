package chat.gptalk.admin.controller;

import chat.gptalk.security.model.AuthUser;
import chat.gptalk.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    @GetMapping("me")
    public AuthUser me() {
        return SecurityUtils.getCurrentUser(AuthUser.class);
    }
}
