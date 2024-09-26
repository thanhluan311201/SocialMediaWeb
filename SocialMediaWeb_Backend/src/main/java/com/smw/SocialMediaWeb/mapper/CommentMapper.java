package com.smw.SocialMediaWeb.mapper;

import com.smw.SocialMediaWeb.dto.request.CommentCreationRequest;
import com.smw.SocialMediaWeb.dto.response.CommentResponse;
import com.smw.SocialMediaWeb.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toComment(CommentCreationRequest request);
    CommentResponse toCommentResponse(Comment comment);
}
