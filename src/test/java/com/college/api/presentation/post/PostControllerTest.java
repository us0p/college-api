package com.college.api.presentation.post;

import com.college.api.application.exception.ResourceNotFoundException;
import com.college.api.application.post.PostService;
import com.college.api.domain.post.Post;
import com.college.api.domain.role.Role;
import com.college.api.domain.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean PostService service;

    private final User user = User.builder().id(1).username("alice")
            .role(Role.builder().id(1).name("student").build()).build();

    private Post buildPost() {
        return Post.builder().id(1).user(user).markdownContent("# Hello")
                .createdAt(OffsetDateTime.now()).updatedAt(OffsetDateTime.now()).build();
    }

    @Test
    void GET_findAllActive_returns200WithList() throws Exception {
        when(service.findAllActive()).thenReturn(List.of(buildPost()));

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].markdownContent").value("# Hello"));
    }

    @Test
    void GET_findById_whenExists_returns200() throws Exception {
        when(service.findById(1)).thenReturn(buildPost());

        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void GET_findById_whenNotFound_returns404() throws Exception {
        when(service.findById(99)).thenThrow(new ResourceNotFoundException("Post", 99));

        mockMvc.perform(get("/api/posts/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void POST_create_withValidBody_returns201() throws Exception {
        when(service.create(eq(1), any())).thenReturn(buildPost());

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PostRequest(1, "# Hello"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.markdownContent").value("# Hello"));
    }

    @Test
    void POST_create_withMissingUserId_returns400() throws Exception {
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"markdownContent\":\"# Hello\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void PUT_update_returns200() throws Exception {
        when(service.update(eq(1), any())).thenReturn(buildPost());

        mockMvc.perform(put("/api/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PostUpdateRequest("# Updated"))))
                .andExpect(status().isOk());
    }

    @Test
    void DELETE_softDelete_returns204() throws Exception {
        doNothing().when(service).softDelete(1);

        mockMvc.perform(delete("/api/posts/1"))
                .andExpect(status().isNoContent());
    }
}
