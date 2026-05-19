package com.college.api.application.post;

import com.college.api.application.exception.ResourceNotFoundException;
import com.college.api.domain.post.Post;
import com.college.api.domain.post.PostCategory;
import com.college.api.domain.post.PostCategoryRepository;
import com.college.api.domain.post.PostRepository;
import com.college.api.domain.user.User;
import com.college.api.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostCategoryRepository postCategoryRepository;

    @Transactional(readOnly = true)
    public List<Post> findAllActive() {
        return postRepository.findAllActive();
    }

    @Transactional(readOnly = true)
    public Post findById(Integer id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", id));
    }

    @Transactional
    public Post create(Integer userId, String title, String markdownContent, Integer categoryId, String coverImgUrl) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        PostCategory category = postCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("PostCategory", categoryId));
        OffsetDateTime now = OffsetDateTime.now();
        Post post = Post.builder()
                .user(user)
                .title(title)
                .markdownContent(markdownContent)
                .coverImgUrl(coverImgUrl)
                .category(category)
                .createdAt(now)
                .updatedAt(now)
                .build();
        return postRepository.save(post);
    }

    @Transactional
    public Post update(Integer id, String title, String markdownContent, Integer categoryId, String coverImgUrl) {
        Post post = findById(id);
        PostCategory category = postCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("PostCategory", categoryId));
        post.setTitle(title);
        post.setMarkdownContent(markdownContent);
        post.setCoverImgUrl(coverImgUrl);
        post.setCategory(category);
        post.setUpdatedAt(OffsetDateTime.now());
        return postRepository.save(post);
    }

    @Transactional
    public void softDelete(Integer id) {
        Post post = findById(id);
        post.setDeletedAt(OffsetDateTime.now());
        postRepository.save(post);
    }

    @Transactional
    public void hardDelete(Integer id) {
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post", id);
        }
        postRepository.deleteById(id);
    }
}
