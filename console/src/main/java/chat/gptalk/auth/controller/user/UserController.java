package chat.gptalk.auth.controller.user;

import chat.gptalk.auth.util.SecurityUtils;
import chat.gptalk.common.security.ConsoleAuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    @GetMapping("me")
    public ConsoleAuthenticatedUser me() {
        return SecurityUtils.getCurrentUser();
    }
}
