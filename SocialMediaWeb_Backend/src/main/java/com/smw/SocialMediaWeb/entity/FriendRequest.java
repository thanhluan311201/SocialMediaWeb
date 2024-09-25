package com.smw.SocialMediaWeb.entity;

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

    LocalDateTime requestTime;

    @Enumerated(EnumType.STRING)
    FriendRequestStatus status;
}

