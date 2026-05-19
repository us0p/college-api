package com.college.api.presentation.post;

import com.college.api.domain.post.Post;

import java.time.OffsetDateTime;

public record PostResponse(
        Integer id,
        Integer userId,
        String username,
        String title,
        String markdownContent,
        String coverImgUrl,
        Integer categoryId,
        String categoryName,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        OffsetDateTime deletedAt
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getUser().getId(),
                post.getUser().getUsername(),
                post.getTitle(),
                post.getMarkdownContent(),
                post.getCoverImgUrl(),
                post.getCategory().getId(),
                post.getCategory().getName(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getDeletedAt()
        );
    }
}
