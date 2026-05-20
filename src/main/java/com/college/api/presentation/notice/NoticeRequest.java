package com.college.api.presentation.notice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NoticeRequest(
        @NotNull Integer userId,
        @NotBlank @Size(max = 200) String title,
        @NotBlank String markdownContent,
        @NotNull Integer categoryId,
        String coverImgUrl
) {}
