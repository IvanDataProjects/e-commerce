package com.gestor.e_commerce.controller;

import com.gestor.e_commerce.dto.OrderDTO;
import com.gestor.e_commerce.dto.request.OrderRequestDTO;
import com.gestor.e_commerce.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/e-commerce/order")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    // CREATE ORDER
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderRequestDTO requestDTO) {
        return ResponseEntity.ok(service.createOrder(requestDTO));
    }

    // GET ALL ORDERS
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(service.getAllOrders());
    }

    // GET ORDER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getOrderById(id));
    }

    // GET TOTAL ORDERS PRICE
    @GetMapping("/total")
    public ResponseEntity<Double> getTotalOrdersPrice() {
        return ResponseEntity.ok(service.getTotalOrdersPrice());
    }

    // GET ORDERS PRICE MORE THAN
    @GetMapping("/expensive")
    public ResponseEntity<List<OrderDTO>> getOrdersPriceMoreThan(@RequestParam Double price) {
        return ResponseEntity.ok(service.getOrdersPriceMoreThan(price));
    }

    // GET ORDERS BY USER
    @GetMapping("/user/{id}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUser(@PathVariable Long id) {
        return ResponseEntity.ok(service.getOrdersByUser(id));
    }

    // DELETE ORDER
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        service.deleteOrder(id);

        return ResponseEntity.noContent().build();
    }
}