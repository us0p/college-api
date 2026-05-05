package com.college.api.presentation.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostRequest(
        @NotNull Integer userId,
        @NotBlank @Size(max = 200) String title,
        @NotBlank String markdownContent
) {}
