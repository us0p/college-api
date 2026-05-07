package com.college.api.presentation.chat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ChatRequest(
        @NotBlank String question,
        @Min(1) @Max(10) Integer contextChunks) {}
