package com.smw.SocialMediaWeb.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999,"Uncategorized Error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001,"Uncategorized Error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1003,"Toi thieu {min} ky tu", HttpStatus.BAD_REQUEST),
    FIELD_BLANKED(1004,"Khong duoc de trong", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1005,"User not found", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006,"Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007,"You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008,"Your age must be atleast {min}", HttpStatus.BAD_REQUEST),
    POST_NOT_FOUND(1009,"Post not found", HttpStatus.NOT_FOUND),
    COMMENT_NOT_FOUND(1010, "Comment not found", HttpStatus.NOT_FOUND),
    ;



    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;


}
