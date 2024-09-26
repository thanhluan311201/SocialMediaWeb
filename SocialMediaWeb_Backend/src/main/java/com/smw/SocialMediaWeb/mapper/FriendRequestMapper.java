package com.smw.SocialMediaWeb.mapper;

import com.smw.SocialMediaWeb.dto.request.SendFriendRequest;
import com.smw.SocialMediaWeb.dto.response.SendFriendRequestResponse;
import com.smw.SocialMediaWeb.entity.FriendRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.aop.framework.adapter.AfterReturningAdviceInterceptor;

@Mapper(componentModel = "spring")
public interface FriendRequestMapper {
    FriendRequest toFriendRequest(SendFriendRequest request);
    SendFriendRequestResponse toSendFriendRequestResponse(FriendRequest friendRequest);
}
