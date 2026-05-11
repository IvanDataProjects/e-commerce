package com.gestor.e_commerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestor.e_commerce.dto.UserDTO;
import com.gestor.e_commerce.dto.request.UserRequestDTO;
import com.gestor.e_commerce.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @Autowired
    private ObjectMapper objectMapper;

    // CREATE - OK
    @Test
    void shouldCreateUser_ok() throws Exception {

        // ARRANGE
        UserRequestDTO request = UserRequestDTO.builder()
                .name("Ivan")
                .email("ivan@email.com")
                .build();

        UserDTO response = UserDTO.builder()
                .id(1L)
                .name("Ivan")
                .email("ivan@email.com")
                .build();

        when(service.createUser(any())).thenReturn(response);

        // ACT + ASSERT
        mockMvc.perform(post("/e-commerce/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ivan"))
                .andExpect(jsonPath("$.email").value("ivan@email.com"));

        verify(service).createUser(any());
    }

    // CREATE - KO (VALIDATION)
    @Test
    void shouldFailCreateUser_whenInvalid() throws Exception {

        UserRequestDTO request = UserRequestDTO.builder()
                .name("")
                .email("invalid")
                .build();

        mockMvc.perform(post("/e-commerce/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, never()).createUser(any());
    }

    // CREATE - KO (EMPTY BODY)
    @Test
    void shouldFailCreateUser_whenEmptyBody() throws Exception {

        mockMvc.perform(post("/e-commerce/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verify(service, never()).createUser(any());
    }

    // GET - OK (NUEVO TEST IMPORTANTE)
    @Test
    void shouldGetUserById_ok() throws Exception {

        UserDTO response = UserDTO.builder()
                .id(1L)
                .name("Ivan")
                .email("ivan@email.com")
                .build();

        when(service.getUserById(1L)).thenReturn(response);

        mockMvc.perform(get("/e-commerce/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ivan"))
                .andExpect(jsonPath("$.email").value("ivan@email.com"));

        verify(service).getUserById(1L);
    }
}