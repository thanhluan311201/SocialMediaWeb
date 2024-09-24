package com.smw.SocialMediaWeb.dto.request;

import com.smw.SocialMediaWeb.validator.DobConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @NotBlank(message = "FIELD_BLANKED")
    String username;

    @Size(min = 8, message = "INVALID_PASSWORD")
    @NotBlank(message = "FIELD_BLANKED")
    String password;

    @NotBlank(message = "FIELD_BLANKED")
    String firstname;

    @NotBlank(message = "FIELD_BLANKED")
    String lastname;

    @DobConstraint(min=16, message = "INVALID_DOB")
    @NotNull(message = "FIELD_BLANKED")
    LocalDate dob;

}
