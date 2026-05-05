package com.college.api.presentation.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank @Size(max = 20) String username,
        @NotNull Integer roleId,
        @Size(max = 10) String ra,
        @NotBlank @Size(min = 8, max = 72) String password,
        @NotBlank @Email @Size(max = 254) String email,
        @Size(max = 20) String phoneNumber
) {}
