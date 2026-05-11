package com.gestor.e_commerce.service;

import com.gestor.e_commerce.dto.UserDTO;
import com.gestor.e_commerce.dto.request.UserRequestDTO;
import com.gestor.e_commerce.exception.EmailAlreadyExistsException;
import com.gestor.e_commerce.exception.UserNotFoundException;
import com.gestor.e_commerce.model.User;
import com.gestor.e_commerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    // ---------------- PRIVATE METHODS ----------------
    private void validateEmail(String email) {
        if (repository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }
    }

    private User toEntity(UserRequestDTO dto) {
        return User.builder().name(dto.getName()).email(dto.getEmail()).build();
    }

    private UserDTO toDTO(User user) {
        return UserDTO.builder().id(user.getId()).name(user.getName()).email(user.getEmail()).build();
    }

    // CREATE USER
    public UserDTO createUser(UserRequestDTO requestDTO) {
        validateEmail(requestDTO.getEmail());
        User userEntity = toEntity(requestDTO);

        User saved = repository.save(userEntity);

        return toDTO(saved);
    }

    // GET USER BY ID
    public UserDTO getUserById(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        return toDTO(user);
    }

    // GET ALL USERS
    public List<UserDTO> getAllUsers() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

}