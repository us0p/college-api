package com.college.api.infrastructure.persistence.post;

import com.college.api.domain.post.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPostCategoryRepository extends JpaRepository<PostCategory, Integer> {
}
