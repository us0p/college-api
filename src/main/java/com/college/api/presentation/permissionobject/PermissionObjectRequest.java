package com.college.api.presentation.permissionobject;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PermissionObjectRequest(
        @NotBlank @Size(max = 20) String name
) {}
