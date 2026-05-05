package com.college.api.presentation.role;

import com.college.api.application.exception.ResourceNotFoundException;
import com.college.api.application.role.RoleService;
import com.college.api.domain.role.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoleController.class)
class RoleControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean RoleService service;

    @Test
    void GET_findAll_returns200WithList() throws Exception {
        when(service.findAll()).thenReturn(List.of(Role.builder().id(1).name("admin").build()));

        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("admin"));
    }

    @Test
    void GET_findById_whenNotFound_returns404() throws Exception {
        when(service.findById(99)).thenThrow(new ResourceNotFoundException("Role", 99));

        mockMvc.perform(get("/api/roles/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void POST_create_withValidBody_returns201() throws Exception {
        when(service.create("admin")).thenReturn(Role.builder().id(1).name("admin").build());

        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RoleRequest("admin"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("admin"));
    }

    @Test
    void POST_create_withBlankName_returns400() throws Exception {
        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RoleRequest(""))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void PUT_update_whenExists_returns200() throws Exception {
        when(service.update(eq(1), any())).thenReturn(Role.builder().id(1).name("student").build());

        mockMvc.perform(put("/api/roles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RoleRequest("student"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("student"));
    }

    @Test
    void DELETE_delete_whenExists_returns204() throws Exception {
        doNothing().when(service).delete(1);

        mockMvc.perform(delete("/api/roles/1"))
                .andExpect(status().isNoContent());
    }
}
