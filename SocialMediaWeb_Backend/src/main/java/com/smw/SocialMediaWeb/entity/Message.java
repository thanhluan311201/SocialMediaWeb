package com.smw.SocialMediaWeb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String content;
    LocalDateTime sentAt;
    Boolean isEdited;

    @ManyToOne
    User sender;

    @ManyToOne
    User receiver;

    @ManyToOne
    @JsonIgnore
    Conversation conversation;
}
