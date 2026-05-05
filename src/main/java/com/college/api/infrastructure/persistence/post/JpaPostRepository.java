package com.college.api.infrastructure.persistence.post;

import com.college.api.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaPostRepository extends JpaRepository<Post, Integer> {

    @Query("SELECT p FROM Post p WHERE p.deletedAt IS NULL")
    List<Post> findAllActive();
}
