package com.smw.SocialMediaWeb.repository;

import com.smw.SocialMediaWeb.entity.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShareRepository extends JpaRepository<Share, String> {
}
