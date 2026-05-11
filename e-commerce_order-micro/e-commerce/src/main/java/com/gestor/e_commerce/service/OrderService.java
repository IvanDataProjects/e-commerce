package com.gestor.e_commerce.service;

import com.gestor.e_commerce.client.UserClient;
import com.gestor.e_commerce.dto.OrderDTO;
import com.gestor.e_commerce.dto.UserDTO;
import com.gestor.e_commerce.dto.request.OrderRequestDTO;
import com.gestor.e_commerce.exception.OrderNotFoundException;
import com.gestor.e_commerce.messaging.producer.OrderProducer;
import com.gestor.e_commerce.model.Order;
import com.gestor.e_commerce.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserClient userClient;
    private final OrderProducer orderProducer;

    public OrderService(OrderRepository orderRepository, UserClient userClient, OrderProducer orderProducer) {
        this.orderRepository = orderRepository;
        this.userClient = userClient;
        this.orderProducer = orderProducer;
    }

    // =========================
    // CONVERTERS
    // =========================

    private OrderDTO mapToDTO(Order order, String username) {
        return OrderDTO.builder().id(order.getId()).productName(order.getProductName()).price(order.getPrice()).quantity(order.getQuantity()).userId(order.getUserId()).username(username).build();
    }

    // =========================
    // USER MAPPING
    // =========================

    // SINGLE USER CALL
    private OrderDTO mapWithUser(Order order) {
        UserDTO user = userClient.getUser(order.getUserId());
        String username = (user != null) ? user.getName() : "UNKNOWN";

        return mapToDTO(order, username);
    }

    // MAP USING PRELOADED USERS
    private OrderDTO mapWithUser(Order order, Map<Long, UserDTO> usersMap) {
        UserDTO user = usersMap.get(order.getUserId());
        String username = (user != null) ? user.getName() : "UNKNOWN";

        return mapToDTO(order, username);
    }

    // =========================
    // HELPERS
    // =========================
    private Order findOrderOrThrow(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    private Map<Long, UserDTO> getUsersMap() {
        return userClient.getAllUsers().stream().collect(Collectors.toMap(UserDTO::getId, user -> user));
    }

    // =========================
    // CREATE ORDER
    // =========================
    public OrderDTO createOrder(OrderRequestDTO requestDTO) {
        // REQUEST_DTO --> ENTITY
        Order orderEntity = Order.builder().productName(requestDTO.getProductName()).price(requestDTO.getPrice()).quantity(requestDTO.getQuantity()).userId(requestDTO.getUserId()).build();

        // SAVE ENTITY
        Order saved = orderRepository.save(orderEntity);

        // SEND EVENT TO KAFKA
        orderProducer.sendOrderCreated();

        // ENTITY --> DTO
        return mapWithUser(saved);
    }

    // =========================
    // GET ALL ORDERS
    // =========================
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        Map<Long, UserDTO> usersMap = getUsersMap();

        return orders.stream().map(order -> mapWithUser(order, usersMap)).toList();
    }

    // =========================
    // GET ORDER BY ID
    // =========================
    public OrderDTO getOrderById(Long id) {
        Order order = findOrderOrThrow(id);

        return mapWithUser(order);
    }

    // =========================
    // GET TOTAL ORDERS PRICE
    // =========================
    public Double getTotalOrdersPrice() {
        return orderRepository.findAll().stream().mapToDouble(Order::getPrice).sum();
    }

    // =========================
    // GET ORDERS PRICE MORE THAN
    // =========================
    public List<OrderDTO> getOrdersPriceMoreThan(Double price) {
        List<Order> orders = orderRepository.findByPriceGreaterThan(price);
        Map<Long, UserDTO> usersMap = getUsersMap();

        return orders.stream().map(order -> mapWithUser(order, usersMap)).toList();
    }

    // =========================
    // GET ORDERS BY USER
    // =========================
    public List<OrderDTO> getOrdersByUser(Long id) {
        return orderRepository.findByUserId(id).stream().map(this::mapWithUser).toList();
    }

    // =========================
    // DELETE ORDER
    // =========================
    public void deleteOrder(Long id) {
        Order order = findOrderOrThrow(id);

        orderRepository.delete(order);
        orderProducer.sendOrderDeleted(order);
    }
}
