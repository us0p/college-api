package com.college.api.presentation.post;

import com.college.api.domain.post.Post;

import java.time.OffsetDateTime;

public record PostResponse(
        Integer id,
        Integer userId,
        String username,
        String markdownContent,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        OffsetDateTime deletedAt
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getUser().getId(),
                post.getUser().getUsername(),
                post.getMarkdownContent(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getDeletedAt()
        );
    }
}
