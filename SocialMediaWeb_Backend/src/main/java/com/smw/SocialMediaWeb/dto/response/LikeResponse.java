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
public class LikeResponse {
    String objectId; // dùng chung cho post, comment và shared post
    LocalDateTime likedAt;
    User user;
}
