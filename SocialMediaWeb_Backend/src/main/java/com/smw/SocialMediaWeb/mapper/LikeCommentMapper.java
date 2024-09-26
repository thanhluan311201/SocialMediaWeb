package com.smw.SocialMediaWeb.mapper;

import com.smw.SocialMediaWeb.dto.request.LikeCommentRequest;
import com.smw.SocialMediaWeb.dto.response.LikeResponse;
import com.smw.SocialMediaWeb.entity.CommentLike;
import com.smw.SocialMediaWeb.entity.PostLike;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LikeCommentMapper {
    CommentLike toCommentLike(LikeCommentRequest request);
    LikeResponse toLikeCommentResponse(PostLike postLike);
}
