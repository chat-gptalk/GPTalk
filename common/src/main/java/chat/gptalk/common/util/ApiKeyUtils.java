package chat.gptalk.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

public final class ApiKeyUtils {

    public static String newKey() {
        return "sk-" + UUID.randomUUID().toString().replace("-", "");
    }

    public static String hash(String key) {
        return hash(key, null);
    }

    public static String hash(String key, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String content = salt != null ? salt + ":" + key : key;
            byte[] hashBytes = digest.digest(content.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}
