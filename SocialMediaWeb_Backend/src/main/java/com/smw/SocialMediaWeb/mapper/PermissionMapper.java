package com.smw.SocialMediaWeb.mapper;


import com.smw.SocialMediaWeb.dto.request.PermissionRequest;
import com.smw.SocialMediaWeb.dto.request.UserCreationRequest;
import com.smw.SocialMediaWeb.dto.request.UserUpdateRequest;
import com.smw.SocialMediaWeb.dto.response.PermissionResponse;
import com.smw.SocialMediaWeb.dto.response.UserResponse;
import com.smw.SocialMediaWeb.entity.Permission;
import com.smw.SocialMediaWeb.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);


}
