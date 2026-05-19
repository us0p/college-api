package com.college.api.application.post;

import com.college.api.application.exception.ResourceNotFoundException;
import com.college.api.domain.post.Post;
import com.college.api.domain.post.PostCategory;
import com.college.api.domain.post.PostCategoryRepository;
import com.college.api.domain.post.PostRepository;
import com.college.api.domain.role.Role;
import com.college.api.domain.user.User;
import com.college.api.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock private PostRepository postRepository;
    @Mock private UserRepository userRepository;
    @Mock private PostCategoryRepository postCategoryRepository;

    @InjectMocks
    private PostService service;

    private final User user = User.builder().id(1).username("alice")
            .role(Role.builder().id(1).name("student").build()).build();
    private final PostCategory category = PostCategory.builder().id(1).name("general").build();

    private Post buildPost() {
        return Post.builder().id(1).user(user).title("Hello").markdownContent("# Hello")
                .category(category).createdAt(OffsetDateTime.now()).updatedAt(OffsetDateTime.now()).build();
    }

    @Test
    void findAllActive_returnsOnlyNonDeletedPosts() {
        when(postRepository.findAllActive()).thenReturn(List.of(buildPost()));

        assertThat(service.findAllActive()).hasSize(1);
    }

    @Test
    void findById_whenExists_returnsPost() {
        Post post = buildPost();
        when(postRepository.findById(1)).thenReturn(Optional.of(post));

        assertThat(service.findById(1)).isEqualTo(post);
    }

    @Test
    void findById_whenNotFound_throwsResourceNotFoundException() {
        when(postRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void create_whenUserAndCategoryExist_savesPost() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(postCategoryRepository.findById(1)).thenReturn(Optional.of(category));
        Post saved = buildPost();
        when(postRepository.save(any())).thenReturn(saved);

        Post result = service.create(1, "Hello", "# Hello", 1, null);

        assertThat(result.getTitle()).isEqualTo("Hello");
        assertThat(result.getMarkdownContent()).isEqualTo("# Hello");
        assertThat(result.getDeletedAt()).isNull();
    }

    @Test
    void create_whenUserNotFound_throwsResourceNotFoundException() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(99, "Hello", "# Hello", 1, null))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void create_whenCategoryNotFound_throwsResourceNotFoundException() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(postCategoryRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(1, "Hello", "# Hello", 99, null))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void update_updatesTitleMarkdownAndCategory() {
        Post existing = buildPost();
        PostCategory newCategory = PostCategory.builder().id(2).name("tech").build();
        when(postRepository.findById(1)).thenReturn(Optional.of(existing));
        when(postCategoryRepository.findById(2)).thenReturn(Optional.of(newCategory));
        when(postRepository.save(existing)).thenReturn(existing);

        Post result = service.update(1, "New", "new", 2, "http://img.url");

        assertThat(result.getTitle()).isEqualTo("New");
        assertThat(result.getMarkdownContent()).isEqualTo("new");
        assertThat(result.getCoverImgUrl()).isEqualTo("http://img.url");
        assertThat(result.getCategory().getName()).isEqualTo("tech");
    }

    @Test
    void softDelete_setsDeletedAt() {
        Post existing = buildPost();
        when(postRepository.findById(1)).thenReturn(Optional.of(existing));
        when(postRepository.save(any())).thenReturn(existing);

        service.softDelete(1);

        assertThat(existing.getDeletedAt()).isNotNull();
        verify(postRepository).save(existing);
    }

    @Test
    void hardDelete_whenExists_deletesById() {
        when(postRepository.existsById(1)).thenReturn(true);

        service.hardDelete(1);

        verify(postRepository).deleteById(1);
    }
}
