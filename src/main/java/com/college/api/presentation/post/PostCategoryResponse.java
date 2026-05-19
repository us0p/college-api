package com.college.api.presentation.post;

import com.college.api.domain.post.PostCategory;

public record PostCategoryResponse(Integer id, String name) {

    public static PostCategoryResponse from(PostCategory category) {
        return new PostCategoryResponse(category.getId(), category.getName());
    }
}
