package com.college.api.infrastructure.persistence.post;

import com.college.api.domain.post.PostCategory;
import com.college.api.domain.post.PostCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostCategoryRepositoryAdapter implements PostCategoryRepository {

    private final JpaPostCategoryRepository jpa;

    @Override
    public PostCategory save(PostCategory postCategory) {
        return jpa.save(postCategory);
    }

    @Override
    public Optional<PostCategory> findById(Integer id) {
        return jpa.findById(id);
    }

    @Override
    public List<PostCategory> findAll() {
        return jpa.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        jpa.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return jpa.existsById(id);
    }
}
