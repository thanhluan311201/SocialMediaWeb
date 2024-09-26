package com.smw.SocialMediaWeb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Share {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    LocalDateTime sharedAt;
    String content;

    @ManyToOne
    User user;

    @JsonIgnore
    @ManyToOne
    Post post;

    @OneToMany(mappedBy = "share", cascade = CascadeType.ALL)
    Set<Comment> comments;

    @OneToMany(mappedBy = "share", cascade = CascadeType.ALL)
    Set<PostLike> likes;
}
