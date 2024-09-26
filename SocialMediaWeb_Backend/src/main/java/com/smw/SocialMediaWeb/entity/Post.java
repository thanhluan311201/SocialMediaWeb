package com.smw.SocialMediaWeb.entity;

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
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String content;
    LocalDateTime createdAt;

    @ManyToOne
    User author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    Set<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    Set<PostLike> likes;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    Set<Share> shares;
}
