package com.smw.SocialMediaWeb.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String username;

    @JsonIgnore
    String password;

    String firstname;
    String lastname;
    String gender;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;


    @ManyToMany
    Set<Role> roles;
}
