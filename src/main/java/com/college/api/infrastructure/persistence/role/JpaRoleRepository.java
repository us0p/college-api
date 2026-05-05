package com.college.api.infrastructure.persistence.role;

import com.college.api.domain.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRoleRepository extends JpaRepository<Role, Integer> {
}
