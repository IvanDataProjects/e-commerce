package com.gestor.e_commerce.exception;

public class OrderNotFoundException extends ResourceNotFoundException {

    public OrderNotFoundException(Long id) {
        super("Order not found with id: " + id);
    }
}
