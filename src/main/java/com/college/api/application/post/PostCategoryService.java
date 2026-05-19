package com.college.api.application.post;

import com.college.api.application.exception.ResourceNotFoundException;
import com.college.api.domain.post.PostCategory;
import com.college.api.domain.post.PostCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostCategoryService {

    private final PostCategoryRepository repository;

    @Transactional(readOnly = true)
    public List<PostCategory> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public PostCategory findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PostCategory", id));
    }

    @Transactional
    public PostCategory create(String name) {
        return repository.save(PostCategory.builder().name(name).build());
    }

    @Transactional
    public PostCategory update(Integer id, String name) {
        PostCategory category = findById(id);
        category.setName(name);
        return repository.save(category);
    }

    @Transactional
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("PostCategory", id);
        }
        repository.deleteById(id);
    }
}
