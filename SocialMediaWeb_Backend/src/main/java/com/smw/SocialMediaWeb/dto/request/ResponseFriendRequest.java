package com.smw.SocialMediaWeb.dto.request;

import com.smw.SocialMediaWeb.enums.FriendRequestStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseFriendRequest {
    String requestId;
    FriendRequestStatus status;
}
