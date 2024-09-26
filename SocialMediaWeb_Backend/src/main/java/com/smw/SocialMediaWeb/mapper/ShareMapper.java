package com.smw.SocialMediaWeb.mapper;

import com.smw.SocialMediaWeb.dto.request.SharePostRequest;
import com.smw.SocialMediaWeb.dto.request.SharedPostUpdateRequest;
import com.smw.SocialMediaWeb.dto.response.SharePostResponse;
import com.smw.SocialMediaWeb.entity.Share;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ShareMapper {
    Share toShare(SharePostRequest request);
    @Mapping(source = "id", target = "shareId")
    @Mapping(source = "post", target = "post")
    SharePostResponse toSharePostResponse(Share share);
    void updateSharedPost(@MappingTarget Share share, SharedPostUpdateRequest request);
}
