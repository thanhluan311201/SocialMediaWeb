package com.smw.SocialMediaWeb.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentCreationRequest {
    String objectId;
    @NotBlank(message = "FIELD_BLANKED")
    String content;
}
