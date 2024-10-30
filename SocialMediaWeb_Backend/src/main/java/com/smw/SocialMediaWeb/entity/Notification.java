package com.smw.SocialMediaWeb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smw.SocialMediaWeb.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String content;

    @Enumerated(EnumType.STRING)
    NotificationType type;

    String redirectUrl;

    @ManyToOne
    User receiver;

    boolean isRead;

    @Temporal(TemporalType.TIMESTAMP)
    Date createdAt;
}
