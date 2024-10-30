package com.smw.SocialMediaWeb.enums;

import lombok.Getter;

@Getter
public enum DefaultNotification {
    POST_LIKE_NOTIFICATION(" liked your post"),
    POST_COMMENT_NOTIFICATION(" commented under your post"),
    POST_SHARE_NOTIFICATION(" shared your post"),
    COMMENT_LIKE_NOTIFICATION(" liked your comment"),
    COMMENT_RESPONSE_NOTIFICATION(" responded your comment"),
    MESSAGE_NOTIFICATION(" has sent you a message"),
    ;


    private final String message;

    DefaultNotification(String message){
        this.message = message;
    }
}