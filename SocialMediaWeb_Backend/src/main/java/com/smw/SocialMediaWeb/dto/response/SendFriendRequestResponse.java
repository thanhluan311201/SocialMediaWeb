package com.smw.SocialMediaWeb.dto.response;

import com.smw.SocialMediaWeb.enums.FriendRequestStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendFriendRequestResponse {
    String requestId;
    FriendRequestStatus status;
    String receiverId;
}
