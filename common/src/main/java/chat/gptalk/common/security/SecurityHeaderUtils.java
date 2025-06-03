package chat.gptalk.common.security;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public final class SecurityHeaderUtils {

    private SecurityHeaderUtils() {
    }

    public static String encodeUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return "";
        }
        return Base64.getEncoder().encodeToString(username.getBytes(StandardCharsets.UTF_8));
    }

    public static String decodeUsername(String usernameEnc) {
        if (!StringUtils.hasText(usernameEnc)) {
            return "";
        }
        return new String(Base64.getDecoder().decode(usernameEnc), StandardCharsets.UTF_8);
    }

    public static String encodeRoles(List<String> roles) {
        if (CollectionUtils.isEmpty(roles)) {
            return "";
        }
        return String.join(",", roles);
    }

    public static List<String> decodeRoles(String rolesInHeader) {
        if (!StringUtils.hasText(rolesInHeader)) {
            return new ArrayList<>();
        }
        return Arrays.stream(rolesInHeader.split(",")).map(String::trim).toList();
    }
}
