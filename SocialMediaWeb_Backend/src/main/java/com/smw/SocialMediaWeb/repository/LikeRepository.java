package com.smw.SocialMediaWeb.repository;

import com.smw.SocialMediaWeb.entity.ObjectLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<ObjectLike, String> {
}
