package com.college.api.application.role;

import com.college.api.application.exception.ResourceNotFoundException;
import com.college.api.domain.role.Role;
import com.college.api.domain.role.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository repository;

    @InjectMocks
    private RoleService service;

    @Test
    void findAll_returnsAllRoles() {
        List<Role> roles = List.of(
                Role.builder().id(1).name("admin").build(),
                Role.builder().id(2).name("student").build()
        );
        when(repository.findAll()).thenReturn(roles);

        assertThat(service.findAll()).hasSize(2);
    }

    @Test
    void findById_whenExists_returnsRole() {
        Role role = Role.builder().id(1).name("admin").build();
        when(repository.findById(1)).thenReturn(Optional.of(role));

        assertThat(service.findById(1)).isEqualTo(role);
    }

    @Test
    void findById_whenNotFound_throwsResourceNotFoundException() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void create_savesRole() {
        Role saved = Role.builder().id(1).name("admin").build();
        when(repository.save(any())).thenReturn(saved);

        Role result = service.create("admin");

        assertThat(result.getName()).isEqualTo("admin");
        verify(repository).save(any(Role.class));
    }

    @Test
    void update_whenExists_updatesName() {
        Role existing = Role.builder().id(1).name("old").build();
        when(repository.findById(1)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        Role result = service.update(1, "new");

        assertThat(result.getName()).isEqualTo("new");
    }

    @Test
    void delete_whenExists_deletesById() {
        when(repository.existsById(1)).thenReturn(true);

        service.delete(1);

        verify(repository).deleteById(1);
    }

    @Test
    void delete_whenNotFound_throwsResourceNotFoundException() {
        when(repository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
