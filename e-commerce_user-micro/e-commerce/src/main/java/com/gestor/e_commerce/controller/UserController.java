package com.gestor.e_commerce.controller;

import com.gestor.e_commerce.dto.UserDTO;
import com.gestor.e_commerce.dto.request.UserRequestDTO;
import com.gestor.e_commerce.exception.UserNotFoundException;
import com.gestor.e_commerce.model.User;
import com.gestor.e_commerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/e-commerce/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    //POST
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserRequestDTO requestDTO) {
        return ResponseEntity.ok(service.createUser(requestDTO));
    }

    //GETTERS
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }
}
