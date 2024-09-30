package com.smw.SocialMediaWeb.mapper;

import com.smw.SocialMediaWeb.dto.request.CommentCreationRequest;
import com.smw.SocialMediaWeb.dto.request.CommentUpdateRequest;
import com.smw.SocialMediaWeb.dto.response.CommentResponse;
import com.smw.SocialMediaWeb.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Named("toCommentResponse")
    CommentResponse toCommentResponse(Comment comment);

    @Named("toPostCommentResponse")
    @Mapping(source = "post",target = "objectId")
    CommentResponse toPostCommentResponse(Comment comment);

    @Named("toShareCommentResponse")
    @Mapping(source = "share", target = "objectId")
    CommentResponse toShareCommentResponse(Comment comment);

    @Named("toCommentReplyResponse")
    @Mapping(source = "parentComment", target = "objectId")
    CommentResponse toCommentReplyResponse(Comment comment);

    void updateComment(@MappingTarget Comment comment, CommentUpdateRequest request);
}
