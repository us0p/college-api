package com.college.api.infrastructure.persistence.user;

import com.college.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    @Query("SELECT COUNT(u) FROM User u WHERE LOWER(u.role.name) = LOWER(:roleName)")
    long countByRoleName(@Param("roleName") String roleName);
}
