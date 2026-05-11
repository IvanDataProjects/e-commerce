package com.gestor.e_commerce.service;

import com.gestor.e_commerce.client.UserClient;
import com.gestor.e_commerce.dto.OrderDTO;
import com.gestor.e_commerce.dto.UserDTO;
import com.gestor.e_commerce.dto.request.OrderRequestDTO;
import com.gestor.e_commerce.exception.OrderNotFoundException;
import com.gestor.e_commerce.exception.UserNotFoundException;
import com.gestor.e_commerce.messaging.producer.OrderProducer;
import com.gestor.e_commerce.model.Order;
import com.gestor.e_commerce.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserClient userClient;

    @Mock
    private OrderProducer orderProducer;

    @InjectMocks
    private OrderService orderService;

    // CREATE - OK
    @Test
    void shouldCreateOrder_ok() {
        OrderRequestDTO request = new OrderRequestDTO();
        request.setProductName("Laptop");
        request.setPrice(1000.0);
        request.setQuantity(1);
        request.setUserId(99L);

        Order savedOrder = Order.builder()
                .id(1L)
                .productName("Laptop")
                .price(1000.0)
                .quantity(1)
                .userId(99L)
                .build();

        UserDTO userDTO = new UserDTO();
        userDTO.setName("Ivan");

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(userClient.getUser(anyLong())).thenReturn(userDTO);

        OrderDTO result = orderService.createOrder(request);

        assertEquals("Laptop", result.getProductName());
        assertEquals("Ivan", result.getUsername());

        verify(orderRepository).save(any(Order.class));
        verify(userClient).getUser(99L);
        verify(orderProducer).sendOrderCreated();
    }

    // GET BY ID - OK
    @Test
    void shouldGetOrderById_ok() {

        Order order = Order.builder()
                .id(1L)
                .productName("Laptop")
                .price(1000.0)
                .quantity(1)
                .userId(1L)
                .build();

        UserDTO userDTO = new UserDTO();
        userDTO.setName("Ivan");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(userClient.getUser(anyLong())).thenReturn(userDTO);

        OrderDTO result = orderService.getOrderById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Laptop", result.getProductName());
        assertEquals("Ivan", result.getUsername());

        verify(orderRepository).findById(1L);
        verify(userClient).getUser(1L);
    }

    // GET BY ID - KO (NOT FOUND)
    @Test
    void shouldFailGetOrderById_whenNotFound() {

        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class,
                () -> orderService.getOrderById(99L));
    }

    // DELETE - OK
    @Test
    void shouldDeleteOrder_ok() {

        Order order = Order.builder()
                .id(1L)
                .productName("Laptop")
                .price(1000.0)
                .quantity(1)
                .userId(1L)
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.deleteOrder(1L);

        verify(orderRepository).delete(order);
    }

    // DELETE - KO (NOT FOUND)
    @Test
    void shouldFailDelete_whenNotFound() {

        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class,
                () -> orderService.deleteOrder(99L));
    }
}