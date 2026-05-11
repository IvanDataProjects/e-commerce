package com.gestor.e_commerce.repository;

import com.gestor.e_commerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByPriceGreaterThan(double price);

    List<Order> findByUserId(Long userId);
}
