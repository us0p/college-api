package com.college.api.domain.post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post);
    Optional<Post> findById(Integer id);
    List<Post> findAll();
    List<Post> findAllActive();
    void deleteById(Integer id);
    boolean existsById(Integer id);
}
