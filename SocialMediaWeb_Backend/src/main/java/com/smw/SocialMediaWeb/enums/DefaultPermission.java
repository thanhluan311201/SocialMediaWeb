package com.smw.SocialMediaWeb.enums;

import lombok.Getter;

@Getter
public enum DefaultPermission {
    POST_WRITE("POST_WRITE", "Can write posts"),
    POST_DELETE("POST_DELETE", "Can delete posts"),
    POST_EDIT("POST_EDIT", "Can edit posts"),
    COMMENT_WRITE("COMMENT_WRITE", "Can write comments"),
    COMMENT_DELETE("COMMENT_DELETE", "Can delete comments"),
    COMMENT_EDIT("COMMENT_EDIT", "Can edit comments"),
    MESSAGE_EDIT("MESSAGE_EDIT", "Can edit messages"),
    MESSAGE_DELETE("MESSAGE_DELETE", "Can delete messages"),
    SHARED_POST_EDIT("SHARED_POST_EDIT", "Can edit shared posts"),
    SHARED_POST_DELETE("SHARED_POST_DELETE", "Can delete shared posts"),
    ;

    private final String name;
    private final String description;

    DefaultPermission(String name, String description) {
        this.name = name;
        this.description = description;
    }
}