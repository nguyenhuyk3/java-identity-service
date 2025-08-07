package com.identity.identity_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.identity.identity_service.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {}
