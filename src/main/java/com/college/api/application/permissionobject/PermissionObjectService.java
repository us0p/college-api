package com.college.api.application.permissionobject;

import com.college.api.application.exception.ResourceNotFoundException;
import com.college.api.domain.permissionobject.PermissionObject;
import com.college.api.domain.permissionobject.PermissionObjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionObjectService {

    private final PermissionObjectRepository repository;

    @Transactional(readOnly = true)
    public List<PermissionObject> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public PermissionObject findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PermissionObject", id));
    }

    @Transactional
    public PermissionObject create(String name) {
        PermissionObject entity = PermissionObject.builder().name(name).build();
        return repository.save(entity);
    }

    @Transactional
    public PermissionObject update(Integer id, String name) {
        PermissionObject entity = findById(id);
        entity.setName(name);
        return repository.save(entity);
    }

    @Transactional
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("PermissionObject", id);
        }
        repository.deleteById(id);
    }
}
