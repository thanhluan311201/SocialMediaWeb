package com.smw.SocialMediaWeb.mapper;


import com.smw.SocialMediaWeb.dto.request.PermissionRequest;
import com.smw.SocialMediaWeb.dto.request.RoleRequest;
import com.smw.SocialMediaWeb.dto.request.UserUpdateRequest;
import com.smw.SocialMediaWeb.dto.response.PermissionResponse;
import com.smw.SocialMediaWeb.dto.response.RoleResponse;
import com.smw.SocialMediaWeb.entity.Permission;
import com.smw.SocialMediaWeb.entity.Role;
import com.smw.SocialMediaWeb.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);

}
