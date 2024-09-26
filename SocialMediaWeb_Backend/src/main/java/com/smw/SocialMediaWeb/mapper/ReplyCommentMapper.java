package com.smw.SocialMediaWeb.mapper;

import com.smw.SocialMediaWeb.dto.request.CommentReplyRequest;
import com.smw.SocialMediaWeb.dto.request.CommentUpdateRequest;
import com.smw.SocialMediaWeb.dto.response.CommentReplyResponse;
import com.smw.SocialMediaWeb.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReplyCommentMapper {
    default Comment toComment(CommentReplyRequest request) {
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        return comment;
    }

    @Mapping(source = "comment.id", target = "parentCommentId")
    CommentReplyResponse toCommentReplyResponse(Comment comment);
    void updateReplyComment(@MappingTarget Comment comment, CommentUpdateRequest request);
}
