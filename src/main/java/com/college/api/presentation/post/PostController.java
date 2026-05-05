package com.college.api.presentation.post;

import com.college.api.application.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Posts", description = "User-authored markdown posts")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService service;

    @Operation(summary = "List all active posts")
    @GetMapping
    public List<PostResponse> findAllActive() {
        return service.findAllActive().stream().map(PostResponse::from).toList();
    }

    @Operation(summary = "Get a post by ID")
    @ApiResponse(responseCode = "404", description = "Post not found")
    @GetMapping("/{id}")
    public PostResponse findById(@PathVariable Integer id) {
        return PostResponse.from(service.findById(id));
    }

    @Operation(summary = "Create a post")
    @ApiResponse(responseCode = "201", description = "Post created")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PostMapping
    public ResponseEntity<PostResponse> create(@Valid @RequestBody PostRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PostResponse.from(service.create(request.userId(), request.title(), request.markdownContent())));
    }

    @Operation(summary = "Update a post's content")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "404", description = "Post not found")
    @PutMapping("/{id}")
    public PostResponse update(@PathVariable Integer id, @Valid @RequestBody PostUpdateRequest request) {
        return PostResponse.from(service.update(id, request.title(), request.markdownContent()));
    }

    @Operation(summary = "Soft-delete a post")
    @ApiResponse(responseCode = "204", description = "Post deleted")
    @ApiResponse(responseCode = "404", description = "Post not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.softDelete(id);
    }
}
