package com.college.api.presentation.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostCategoryRequest(@NotBlank @Size(max = 20) String name) {}
