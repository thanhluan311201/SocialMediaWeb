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
public class PostResponse {
    String postId;
    String content;
    User user;
    LocalDateTime createdAt;
    Integer likeCount;
}
