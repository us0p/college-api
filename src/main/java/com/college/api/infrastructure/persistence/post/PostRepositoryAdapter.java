package com.college.api.infrastructure.persistence.post;

import com.college.api.domain.post.Post;
import com.college.api.domain.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepositoryAdapter implements PostRepository {

    private final JpaPostRepository jpa;

    @Override
    public Post save(Post post) {
        return jpa.save(post);
    }

    @Override
    public Optional<Post> findById(Integer id) {
        return jpa.findById(id);
    }

    @Override
    public List<Post> findAll() {
        return jpa.findAll();
    }

    @Override
    public List<Post> findAllActive() {
        return jpa.findAllActive();
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
