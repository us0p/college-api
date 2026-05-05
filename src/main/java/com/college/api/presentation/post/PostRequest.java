package com.college.api.presentation.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostRequest(
        @NotNull Integer userId,
        @NotBlank String markdownContent
) {}
