package com.college.api.application.permissionobject;

import com.college.api.application.exception.ResourceNotFoundException;
import com.college.api.domain.permissionobject.PermissionObject;
import com.college.api.domain.permissionobject.PermissionObjectRepository;
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
class PermissionObjectServiceTest {

    @Mock
    private PermissionObjectRepository repository;

    @InjectMocks
    private PermissionObjectService service;

    @Test
    void findAll_returnsAllPermissionObjects() {
        List<PermissionObject> expected = List.of(
                PermissionObject.builder().id(1).name("posts").build(),
                PermissionObject.builder().id(2).name("documents").build()
        );
        when(repository.findAll()).thenReturn(expected);

        List<PermissionObject> result = service.findAll();

        assertThat(result).hasSize(2).isEqualTo(expected);
    }

    @Test
    void findById_whenExists_returnsPermissionObject() {
        PermissionObject p = PermissionObject.builder().id(1).name("posts").build();
        when(repository.findById(1)).thenReturn(Optional.of(p));

        PermissionObject result = service.findById(1);

        assertThat(result).isEqualTo(p);
    }

    @Test
    void findById_whenNotFound_throwsResourceNotFoundException() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void create_savesAndReturnsPermissionObject() {
        PermissionObject saved = PermissionObject.builder().id(1).name("posts").build();
        when(repository.save(any())).thenReturn(saved);

        PermissionObject result = service.create("posts");

        assertThat(result.getName()).isEqualTo("posts");
        verify(repository).save(any(PermissionObject.class));
    }

    @Test
    void update_whenExists_updatesName() {
        PermissionObject existing = PermissionObject.builder().id(1).name("old").build();
        when(repository.findById(1)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        PermissionObject result = service.update(1, "new");

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
