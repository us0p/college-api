package com.college.api.presentation.role;

import com.college.api.application.role.RolePermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Role Permissions", description = "Assign and revoke permissions on roles")
@RestController
@RequestMapping("/api/role-permissions")
@RequiredArgsConstructor
public class RolePermissionController {

    private final RolePermissionService service;

    @Operation(summary = "List all role-permission assignments")
    @GetMapping
    public List<RolePermissionResponse> findAll() {
        return service.findAll().stream().map(RolePermissionResponse::from).toList();
    }

    @Operation(summary = "Get a role-permission assignment by ID")
    @ApiResponse(responseCode = "404", description = "Assignment not found")
    @GetMapping("/{id}")
    public RolePermissionResponse findById(@PathVariable Integer id) {
        return RolePermissionResponse.from(service.findById(id));
    }

    @Operation(summary = "List all permissions assigned to a role")
    @GetMapping("/by-role/{roleId}")
    public List<RolePermissionResponse> findByRoleId(@PathVariable Integer roleId) {
        return service.findByRoleId(roleId).stream().map(RolePermissionResponse::from).toList();
    }

    @Operation(summary = "Assign a permission to a role")
    @ApiResponse(responseCode = "201", description = "Permission assigned")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "404", description = "Role or permission object not found")
    @PostMapping
    public ResponseEntity<RolePermissionResponse> create(@Valid @RequestBody RolePermissionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RolePermissionResponse.from(service.create(request.roleId(), request.permissionId())));
    }

    @Operation(summary = "Revoke a permission from a role")
    @ApiResponse(responseCode = "204", description = "Permission revoked")
    @ApiResponse(responseCode = "404", description = "Assignment not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
