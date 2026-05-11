package com.gestor.e_commerce.service;

import com.gestor.e_commerce.dto.UserDTO;
import com.gestor.e_commerce.dto.request.UserRequestDTO;
import com.gestor.e_commerce.exception.EmailAlreadyExistsException;
import com.gestor.e_commerce.model.User;
import com.gestor.e_commerce.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService userService;


    // =========================
    // CREATE - OK
    // =========================
    @Test
    void shouldCreateUser_ok() {

        UserRequestDTO request = UserRequestDTO.builder()
                .name("Ivan")
                .email("ivan@email.com")
                .build();

        User savedUser = User.builder()
                .id(1L)
                .name("Ivan")
                .email("ivan@email.com")
                .build();

        when(repository.existsByEmail("ivan@email.com"))
                .thenReturn(false);

        when(repository.save(any()))
                .thenReturn(savedUser);

        UserDTO result = userService.createUser(request);

        assertEquals("Ivan", result.getName());
        assertEquals("ivan@email.com", result.getEmail());

        verify(repository).save(any(User.class));
    }


    // =========================
    // CREATE - KO (EMAIL EXISTS)
    // =========================
    @Test
    void shouldFailCreateUser_whenEmailAlreadyExists() {

        UserRequestDTO request = UserRequestDTO.builder()
                .name("Ivan")
                .email("ivan@email.com")
                .build();

        when(repository.existsByEmail("ivan@email.com"))
                .thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class,
                () -> userService.createUser(request));

        verify(repository, never()).save(any());
    }


    // =========================
    // CREATE - OK (SAVE CHECK)
    // =========================
    @Test
    void shouldCallSave_whenUserIsValid() {

        UserRequestDTO request = UserRequestDTO.builder()
                .name("Ivan")
                .email("ivan@email.com")
                .build();

        when(repository.existsByEmail(any()))
                .thenReturn(false);

        when(repository.save(any()))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    user.setId(1L);
                    return user;
                });

        userService.createUser(request);

        verify(repository).save(any(User.class));
    }
}
