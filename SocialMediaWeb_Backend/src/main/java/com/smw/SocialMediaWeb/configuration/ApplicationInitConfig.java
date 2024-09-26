package com.smw.SocialMediaWeb.configuration;

import com.smw.SocialMediaWeb.entity.Permission;
import com.smw.SocialMediaWeb.entity.Role;
import com.smw.SocialMediaWeb.entity.User;
import com.smw.SocialMediaWeb.enums.DefaultPermission;
import com.smw.SocialMediaWeb.enums.DefaultRole;
import com.smw.SocialMediaWeb.repository.PermissionRepository;
import com.smw.SocialMediaWeb.repository.RoleRepository;
import com.smw.SocialMediaWeb.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    PermissionRepository permissionRepository;

    @Bean
    @ConditionalOnProperty(prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            Permission writeCommentPermission = createPermissionIfNotFound(DefaultPermission.COMMENT_WRITE.getName(),
                    DefaultPermission.COMMENT_WRITE.getDescription());
            Permission writePostPermission = createPermissionIfNotFound(DefaultPermission.POST_WRITE.getName(),
                    DefaultPermission.POST_WRITE.getDescription());
            Permission editCommentPermission = createPermissionIfNotFound(DefaultPermission.COMMENT_EDIT.getName(),
                    DefaultPermission.COMMENT_EDIT.getDescription());
            Permission editPostPermission = createPermissionIfNotFound(DefaultPermission.POST_EDIT.getName(),
                    DefaultPermission.POST_EDIT.getDescription());
            Permission editMessagePermission = createPermissionIfNotFound(DefaultPermission.MESSAGE_EDIT.getName(),
                    DefaultPermission.MESSAGE_EDIT.getDescription());
            Permission editSharedPostPermission = createPermissionIfNotFound(DefaultPermission.SHARED_POST_EDIT.getName(),
                    DefaultPermission.SHARED_POST_EDIT.getDescription());
            Permission deleteCommentPermission = createPermissionIfNotFound(DefaultPermission.COMMENT_DELETE.getName(),
                    DefaultPermission.COMMENT_DELETE.getDescription());
            Permission deletePostPermission = createPermissionIfNotFound(DefaultPermission.POST_DELETE.getName(),
                    DefaultPermission.POST_DELETE.getDescription());
            Permission deleteMessagePermission = createPermissionIfNotFound(DefaultPermission.MESSAGE_DELETE.getName(),
                    DefaultPermission.MESSAGE_DELETE.getDescription());
            Permission deleteSharedPostPermission = createPermissionIfNotFound(DefaultPermission.SHARED_POST_DELETE.getName(),
                    DefaultPermission.SHARED_POST_DELETE.getDescription());

            Role adminRole = roleRepository.findById(DefaultRole.ADMIN.getName()).orElse(Role.builder()
                    .name(DefaultRole.ADMIN.getName())
                    .description(DefaultRole.ADMIN.getDescription())
                    .permissions(Set.of(writeCommentPermission, writePostPermission, editCommentPermission,
                            editPostPermission, editMessagePermission, editSharedPostPermission, deleteCommentPermission,
                                deletePostPermission, deleteMessagePermission, deleteSharedPostPermission)) // Gán permissions cho role
                    .build());

            Role userRole = roleRepository.findById(DefaultRole.USER.getName()).orElse(Role.builder()
                    .name(DefaultRole.USER.getName())
                    .description(DefaultRole.USER.getDescription())
                    .permissions(Set.of(writeCommentPermission, writePostPermission))
                    .build());

            Role moderatorRole = roleRepository.findById(DefaultRole.MODERATOR.getName()).orElse(Role.builder()
                    .name(DefaultRole.MODERATOR.getName())
                    .description(DefaultRole.MODERATOR.getDescription())
                    .permissions(Set.of(writeCommentPermission, writePostPermission, editCommentPermission,
                            editPostPermission, editMessagePermission, editSharedPostPermission, deleteCommentPermission,
                            deletePostPermission, deleteMessagePermission, deleteSharedPostPermission)) // Gán permissions cho role
                    .build());

            // Lưu role nếu chưa tồn tại
            if (!adminRole.getName().equalsIgnoreCase("admin")) {
                roleRepository.save(adminRole);
            }

            if (!userRole.getName().equalsIgnoreCase("user")) {
                roleRepository.save(userRole);
            }

            if (!moderatorRole.getName().equalsIgnoreCase("moderator")) {
                roleRepository.save(moderatorRole);
            }

            // Tạo user admin nếu chưa tồn tại
            if (userRepository.findByUsername("admin").isEmpty()) {
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(Set.of(adminRole))
                        .build();

                userRepository.save(user);
                log.warn("admin user has been created with default password, please change it");


            }
        };
    }

    private Permission createPermissionIfNotFound(String name, String description) {
        return permissionRepository.findByName(name).orElseGet(() -> {
            Permission permission = Permission.builder()
                    .name(name)
                    .description(description)
                    .build();
            return permissionRepository.save(permission);
        });
    }
}