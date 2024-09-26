package com.smw.SocialMediaWeb.mapper;

import com.smw.SocialMediaWeb.dto.request.LikePostRequest;
import com.smw.SocialMediaWeb.dto.response.LikeResponse;
import com.smw.SocialMediaWeb.entity.PostLike;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LikePostMapper {
    PostLike toPostLike(LikePostRequest request);
    LikeResponse toLikeResponse(PostLike postLike);
}
