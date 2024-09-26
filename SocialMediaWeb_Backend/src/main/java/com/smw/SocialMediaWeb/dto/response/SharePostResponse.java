package com.smw.SocialMediaWeb.dto.response;

import com.smw.SocialMediaWeb.entity.Post;
import com.smw.SocialMediaWeb.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SharePostResponse {
    String shareId;
    Post post;
    User user;
    String content;
    LocalDateTime sharedAt;
}
