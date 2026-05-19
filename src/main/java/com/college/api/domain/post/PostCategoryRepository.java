package com.college.api.domain.post;

import java.util.List;
import java.util.Optional;

public interface PostCategoryRepository {
    PostCategory save(PostCategory postCategory);
    Optional<PostCategory> findById(Integer id);
    List<PostCategory> findAll();
    void deleteById(Integer id);
    boolean existsById(Integer id);
}
