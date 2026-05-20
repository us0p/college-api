package com.college.api.domain.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Integer id);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    long countByRoleName(String roleName);
    void deleteById(Integer id);
    boolean existsById(Integer id);
}
