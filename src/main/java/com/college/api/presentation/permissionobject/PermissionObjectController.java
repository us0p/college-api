package com.college.api.presentation.permissionobject;

import com.college.api.application.permissionobject.PermissionObjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Permission Objects", description = "Named resources that can be granted to roles (e.g. posts, documents)")
@RestController
@RequestMapping("/api/permission-objects")
@RequiredArgsConstructor
public class PermissionObjectController {

    private final PermissionObjectService service;

    @Operation(summary = "List all permission objects")
    @GetMapping
    public List<PermissionObjectResponse> findAll() {
        return service.findAll().stream().map(PermissionObjectResponse::from).toList();
    }

    @Operation(summary = "Get a permission object by ID")
    @ApiResponse(responseCode = "404", description = "Permission object not found")
    @GetMapping("/{id}")
    public PermissionObjectResponse findById(@PathVariable Integer id) {
        return PermissionObjectResponse.from(service.findById(id));
    }

    @Operation(summary = "Create a permission object")
    @ApiResponse(responseCode = "201", description = "Permission object created")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @PostMapping
    public ResponseEntity<PermissionObjectResponse> create(@Valid @RequestBody PermissionObjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PermissionObjectResponse.from(service.create(request.name())));
    }

    @Operation(summary = "Update a permission object's name")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "404", description = "Permission object not found")
    @PutMapping("/{id}")
    public PermissionObjectResponse update(@PathVariable Integer id,
                                           @Valid @RequestBody PermissionObjectRequest request) {
        return PermissionObjectResponse.from(service.update(id, request.name()));
    }

    @Operation(summary = "Delete a permission object")
    @ApiResponse(responseCode = "204", description = "Permission object deleted")
    @ApiResponse(responseCode = "404", description = "Permission object not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
