package chat.gptalk.common.constants;

import lombok.Getter;

@Getter
public enum EntityStatus {
    ACTIVE(1, "Active"),
    DISABLED(0, "Disabled"),
    LOCKED(-1, "Locked"),
    EXPIRED(-2, "Expired"),
    DELETED(-3, "Deleted");

    private final int code;
    private final String label;

    EntityStatus(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public static EntityStatus fromCode(int code) {
        for (EntityStatus status : values()) {
            if (status.code == code) return status;
        }
        throw new IllegalArgumentException("Unknown status code: " + code);
    }
}
