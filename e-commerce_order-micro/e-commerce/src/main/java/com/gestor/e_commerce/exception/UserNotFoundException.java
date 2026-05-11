package com.gestor.e_commerce.exception;

public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException(Long id) {
        super("User not found with id: " + id);
    }
}
