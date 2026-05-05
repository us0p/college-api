package com.college.api.presentation.role;

import com.college.api.application.role.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Roles", description = "Role management")
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService service;

    @Operation(summary = "List all roles")
    @GetMapping
    public List<RoleResponse> findAll() {
        return service.findAll().stream().map(RoleResponse::from).toList();
    }

    @Operation(summary = "Get a role by ID")
    @ApiResponse(responseCode = "404", description = "Role not found")
    @GetMapping("/{id}")
    public RoleResponse findById(@PathVariable Integer id) {
        return RoleResponse.from(service.findById(id));
    }

    @Operation(summary = "Create a role")
    @ApiResponse(responseCode = "201", description = "Role created")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @PostMapping
    public ResponseEntity<RoleResponse> create(@Valid @RequestBody RoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RoleResponse.from(service.create(request.name())));
    }

    @Operation(summary = "Update a role's name")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "404", description = "Role not found")
    @PutMapping("/{id}")
    public RoleResponse update(@PathVariable Integer id, @Valid @RequestBody RoleRequest request) {
        return RoleResponse.from(service.update(id, request.name()));
    }

    @Operation(summary = "Delete a role")
    @ApiResponse(responseCode = "204", description = "Role deleted")
    @ApiResponse(responseCode = "404", description = "Role not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
