package com.smw.SocialMediaWeb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.smw.SocialMediaWeb.enums.FriendRequestStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    User requester;

    @ManyToOne
    User receiver;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    LocalDateTime requestTime;

    @Enumerated(EnumType.STRING)
    FriendRequestStatus status;
}

