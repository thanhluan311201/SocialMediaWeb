package com.smw.SocialMediaWeb.mapper;

import com.smw.SocialMediaWeb.dto.request.ResponseFriendRequest;
import com.smw.SocialMediaWeb.dto.request.SendFriendRequest;
import com.smw.SocialMediaWeb.dto.response.FriendResponse;
import com.smw.SocialMediaWeb.dto.response.SendFriendRequestResponse;
import com.smw.SocialMediaWeb.entity.Friend;
import com.smw.SocialMediaWeb.entity.FriendRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FriendMapper {
    FriendRequest toFriendRequest(SendFriendRequest request);

    SendFriendRequestResponse toSendFriendRequestResponse(FriendRequest friendRequest);

    Friend toFriend(ResponseFriendRequest request);

    FriendResponse toFriendResponse(Friend friend);
}
