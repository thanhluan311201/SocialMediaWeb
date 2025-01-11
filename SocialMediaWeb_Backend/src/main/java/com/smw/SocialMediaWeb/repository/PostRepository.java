package com.smw.SocialMediaWeb.repository;

import com.smw.SocialMediaWeb.entity.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> findPostsByAuthorId(String userId, Sort sort);
}
