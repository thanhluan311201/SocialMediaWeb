package com.smw.SocialMediaWeb.mapper;

import com.smw.SocialMediaWeb.dto.request.MessageRequest;
import com.smw.SocialMediaWeb.dto.request.MessageUpdateRequest;
import com.smw.SocialMediaWeb.dto.response.MessageResponse;
import com.smw.SocialMediaWeb.entity.Conversation;
import com.smw.SocialMediaWeb.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    Message toMessage(MessageRequest request);
    @Mapping(source = "sender", target = "sender")
    @Mapping(source = "id", target = "messageId")
    MessageResponse toMessageResponse(Message message);
    void updateMessage(@MappingTarget Message message, MessageUpdateRequest request);
}
