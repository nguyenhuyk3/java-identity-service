package com.identity.identity_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.identity.identity_service.entities.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {}
