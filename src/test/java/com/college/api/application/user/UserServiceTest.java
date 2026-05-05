package com.college.api.application.user;

import com.college.api.application.exception.ResourceNotFoundException;
import com.college.api.domain.role.Role;
import com.college.api.domain.role.RoleRepository;
import com.college.api.domain.user.User;
import com.college.api.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService service;

    private final Role role = Role.builder().id(1).name("student").build();

    @Test
    void findAll_returnsAllUsers() {
        List<User> users = List.of(
                User.builder().id(1).username("alice").role(role).build()
        );
        when(userRepository.findAll()).thenReturn(users);

        assertThat(service.findAll()).hasSize(1);
    }

    @Test
    void findById_whenExists_returnsUser() {
        User user = User.builder().id(1).username("alice").role(role).build();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        assertThat(service.findById(1)).isEqualTo(user);
    }

    @Test
    void findById_whenNotFound_throwsResourceNotFoundException() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void create_whenRoleExists_savesUserWithHashedPassword() {
        when(roleRepository.findById(1)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("secret123")).thenReturn("$2a$10$hashedpassword");
        User saved = User.builder().id(1).username("alice").passwordHash("$2a$10$hashedpassword")
                .email("alice@example.com").phoneNumber("11999990000").role(role).ra("RA001").build();
        when(userRepository.save(any())).thenReturn(saved);

        User result = service.create("alice", "secret123", "alice@example.com", "11999990000", 1, "RA001");

        assertThat(result.getUsername()).isEqualTo("alice");
        assertThat(result.getEmail()).isEqualTo("alice@example.com");
        assertThat(result.getPhoneNumber()).isEqualTo("11999990000");
        assertThat(result.getRa()).isEqualTo("RA001");
        assertThat(result.getPasswordHash()).isEqualTo("$2a$10$hashedpassword");
        verify(passwordEncoder).encode("secret123");
    }

    @Test
    void create_whenRoleNotFound_throwsResourceNotFoundException() {
        when(roleRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create("alice", "secret123", "alice@example.com", null, 99, null))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void update_updatesFieldsWithHashedPassword() {
        Role newRole = Role.builder().id(2).name("admin").build();
        User existing = User.builder().id(1).username("alice").passwordHash("oldhash")
                .email("alice@example.com").role(role).build();
        when(userRepository.findById(1)).thenReturn(Optional.of(existing));
        when(roleRepository.findById(2)).thenReturn(Optional.of(newRole));
        when(passwordEncoder.encode("newpass1")).thenReturn("$2a$10$newhash");
        when(userRepository.save(existing)).thenReturn(existing);

        User result = service.update(1, "bob", "newpass1", "bob@example.com", "11888880000", 2, "RA002");

        assertThat(result.getUsername()).isEqualTo("bob");
        assertThat(result.getRole()).isEqualTo(newRole);
        assertThat(result.getPasswordHash()).isEqualTo("$2a$10$newhash");
        assertThat(result.getEmail()).isEqualTo("bob@example.com");
    }

    @Test
    void delete_whenExists_deletesById() {
        when(userRepository.existsById(1)).thenReturn(true);

        service.delete(1);

        verify(userRepository).deleteById(1);
    }
}
