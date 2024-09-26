package com.smw.SocialMediaWeb.dto.response;

import com.smw.SocialMediaWeb.entity.Comment;
import com.smw.SocialMediaWeb.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    String commentId;
    String content;
    User user;
    LocalDateTime commentedAt;
    Set<Comment> replies;
}
