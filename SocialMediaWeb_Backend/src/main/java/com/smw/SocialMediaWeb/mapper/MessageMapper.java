package com.smw.SocialMediaWeb.mapper;

import com.smw.SocialMediaWeb.dto.request.MessageRequest;
import com.smw.SocialMediaWeb.dto.request.MessageUpdateRequest;
import com.smw.SocialMediaWeb.dto.response.MessageResponse;
import com.smw.SocialMediaWeb.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    Message toMessage(MessageRequest request);
    MessageResponse toMessageResponse(Message message);
    void updateMessage(@MappingTarget Message message, MessageUpdateRequest request);
}
