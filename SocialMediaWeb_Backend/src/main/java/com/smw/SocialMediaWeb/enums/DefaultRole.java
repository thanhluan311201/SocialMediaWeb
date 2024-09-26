package com.smw.SocialMediaWeb.enums;

import lombok.Getter;

@Getter
public enum DefaultRole {
    USER("USER", "User role"),
    ADMIN("ADMIN", "Admin role"),
    MODERATOR("MODERATOR", "Moderator role"),
    ;

    private final String name;
    private final String description;

    DefaultRole(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
