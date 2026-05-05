package com.college.api.application.user;

import com.college.api.application.exception.ResourceNotFoundException;
import com.college.api.domain.role.Role;
import com.college.api.domain.role.RoleRepository;
import com.college.api.domain.user.User;
import com.college.api.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    @Transactional
    public User create(String username, Integer roleId, String ra) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", roleId));
        User user = User.builder()
                .username(username)
                .role(role)
                .ra(ra)
                .build();
        return userRepository.save(user);
    }

    @Transactional
    public User update(Integer id, String username, Integer roleId, String ra) {
        User user = findById(id);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", roleId));
        user.setUsername(username);
        user.setRole(role);
        user.setRa(ra);
        return userRepository.save(user);
    }

    @Transactional
    public void delete(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", id);
        }
        userRepository.deleteById(id);
    }
}
