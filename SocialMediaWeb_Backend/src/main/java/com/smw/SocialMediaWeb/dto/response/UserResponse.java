package com.smw.SocialMediaWeb.dto.response;

import com.smw.SocialMediaWeb.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String firstname;
    String lastname;
    String gender;
    LocalDate dob;
    Set<RoleResponse> roles;
}
