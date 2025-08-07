package biojj.managerproductapi.domain.enums;

import lombok.Getter;

@Getter
public enum Profile {
    ADMIN(0, "ROLE_ADMIN"),
    USER(1, "ROLE_USER");

    private final int code;
    private final String description;

    Profile(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static Profile fromCode(int code) {
        for (Profile profile : values()) {
            if (profile.code == code) {
                return profile;
            }
        }
        throw new IllegalArgumentException("Invalid profile code: " + code);
    }
}