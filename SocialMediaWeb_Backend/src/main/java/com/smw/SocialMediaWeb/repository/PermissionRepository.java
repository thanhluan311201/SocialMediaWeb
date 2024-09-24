package com.smw.SocialMediaWeb.repository;

import com.smw.SocialMediaWeb.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
}
