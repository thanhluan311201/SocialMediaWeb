package com.smw.SocialMediaWeb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class ObjectLike {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    LocalDateTime likedAt;

    @ManyToOne
    User user;

    @JsonIgnore
    @ManyToOne
    Post post;

    @JsonIgnore
    @ManyToOne
    Share share;

    @JsonIgnore
    @ManyToOne
    Comment comment;
}
