package com.smw.SocialMediaWeb.repository;

import com.smw.SocialMediaWeb.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, String> {
}
