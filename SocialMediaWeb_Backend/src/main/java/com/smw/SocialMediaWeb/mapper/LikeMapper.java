package com.smw.SocialMediaWeb.mapper;

import com.smw.SocialMediaWeb.dto.response.LikeResponse;
import com.smw.SocialMediaWeb.entity.ObjectLike;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LikeMapper {
    @Mapping(target = "objectId", source = "post")
    LikeResponse toPostLikeResponse(ObjectLike objectLike);

    @Mapping(target = "objectId", source = "share")
    LikeResponse toShareLikeResponse(ObjectLike objectLike);

    @Mapping(target = "objectId", source = "comment")
    LikeResponse toCommentLikeResponse(ObjectLike objectLike);

}
