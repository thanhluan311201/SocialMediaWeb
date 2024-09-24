package com.smw.SocialMediaWeb.mapper;


import com.smw.SocialMediaWeb.dto.request.UserCreationRequest;
import com.smw.SocialMediaWeb.dto.request.UserUpdateRequest;
import com.smw.SocialMediaWeb.dto.response.UserResponse;
import com.smw.SocialMediaWeb.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
