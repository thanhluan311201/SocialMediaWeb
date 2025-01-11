package com.smw.SocialMediaWeb.repository;

import com.smw.SocialMediaWeb.entity.Share;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShareRepository extends JpaRepository<Share, String> {
    List<Share> findSharedPostsByUserId(String userId, Sort sort);
}
