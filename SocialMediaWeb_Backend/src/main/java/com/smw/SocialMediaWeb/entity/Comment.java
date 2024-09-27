package com.smw.SocialMediaWeb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Comment{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String content;
    LocalDateTime commentedAt;

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
    Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
    Set<Comment> replies;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    Set<ObjectLike> likes;
}
