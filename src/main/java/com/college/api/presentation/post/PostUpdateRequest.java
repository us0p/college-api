package com.college.api.presentation.post;

import jakarta.validation.constraints.NotBlank;

public record PostUpdateRequest(@NotBlank String markdownContent) {}
