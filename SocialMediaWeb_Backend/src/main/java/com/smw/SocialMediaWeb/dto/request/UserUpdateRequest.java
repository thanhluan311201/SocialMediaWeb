package com.smw.SocialMediaWeb.dto.request;

import com.smw.SocialMediaWeb.validator.DobConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    @Size(min = 8, message = "INVALID_PASSWORD")
    @NotBlank(message = "FIELD_BLANKED")
    String password;
    String firstname;
    String lastname;
    String gender;
    
    @DobConstraint(min=18, message = "INVALID_DOB")
    LocalDate dob;
    Set<String> roles;
}
