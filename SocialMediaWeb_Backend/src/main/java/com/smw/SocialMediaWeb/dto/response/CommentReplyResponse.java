package com.smw.SocialMediaWeb.dto.response;

import com.smw.SocialMediaWeb.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentReplyResponse {
    String replyId;
    String parentCommentId;
    String content;
    User user;
    LocalDateTime repliedAt;
}
