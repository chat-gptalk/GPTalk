package chat.gptalk.security.model;

import java.util.UUID;

public interface AuthenticatedUser {

    UUID userId();

    UUID tenantId();
}
