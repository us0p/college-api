package com.college.api.presentation.permissionobject;

import com.college.api.application.exception.ResourceNotFoundException;
import com.college.api.application.permissionobject.PermissionObjectService;
import com.college.api.domain.permissionobject.PermissionObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PermissionObjectController.class)
class PermissionObjectControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean PermissionObjectService service;

    @Test
    void GET_findAll_returns200WithList() throws Exception {
        when(service.findAll()).thenReturn(List.of(
                PermissionObject.builder().id(1).name("posts").build()
        ));

        mockMvc.perform(get("/api/permission-objects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("posts"));
    }

    @Test
    void GET_findById_whenExists_returns200() throws Exception {
        when(service.findById(1)).thenReturn(PermissionObject.builder().id(1).name("posts").build());

        mockMvc.perform(get("/api/permission-objects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("posts"));
    }

    @Test
    void GET_findById_whenNotFound_returns404() throws Exception {
        when(service.findById(99)).thenThrow(new ResourceNotFoundException("PermissionObject", 99));

        mockMvc.perform(get("/api/permission-objects/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void POST_create_withValidBody_returns201() throws Exception {
        when(service.create("posts")).thenReturn(PermissionObject.builder().id(1).name("posts").build());

        mockMvc.perform(post("/api/permission-objects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PermissionObjectRequest("posts"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void POST_create_withBlankName_returns400() throws Exception {
        mockMvc.perform(post("/api/permission-objects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PermissionObjectRequest(""))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void PUT_update_whenExists_returns200() throws Exception {
        when(service.update(eq(1), any())).thenReturn(PermissionObject.builder().id(1).name("docs").build());

        mockMvc.perform(put("/api/permission-objects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PermissionObjectRequest("docs"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("docs"));
    }

    @Test
    void DELETE_delete_whenExists_returns204() throws Exception {
        doNothing().when(service).delete(1);

        mockMvc.perform(delete("/api/permission-objects/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void DELETE_delete_whenNotFound_returns404() throws Exception {
        doThrow(new ResourceNotFoundException("PermissionObject", 99)).when(service).delete(99);

        mockMvc.perform(delete("/api/permission-objects/99"))
                .andExpect(status().isNotFound());
    }
}
