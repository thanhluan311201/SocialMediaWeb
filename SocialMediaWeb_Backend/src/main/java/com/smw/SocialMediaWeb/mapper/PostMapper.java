package com.smw.SocialMediaWeb.mapper;

import com.smw.SocialMediaWeb.dto.request.PostCreationRequest;
import com.smw.SocialMediaWeb.dto.request.PostUpdateRequest;
import com.smw.SocialMediaWeb.dto.response.PostResponse;

import com.smw.SocialMediaWeb.entity.Post;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toPost(PostCreationRequest request);
    @Mapping(source = "id", target = "postId")
    @Mapping(source = "author", target = "user")
    PostResponse toPostResponse(Post post);
    void updatePost(@MappingTarget Post post, PostUpdateRequest request);
}
