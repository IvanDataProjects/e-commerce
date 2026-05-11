package com.gestor.e_commerce.messaging.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {
    // This method is triggered when a message arrives in "orders-topic"
    @KafkaListener(topics = "orders-topic", groupId = "user-service-group")
    public void consume(String message) {
        if ("ORDER_CREATED".equals(message)) {
            System.out.println("Order created");
        } else if ("ORDER_DELETED".equals(message)) {
            System.out.println("Order deleted");
        } else {
            System.out.println("Order not found");
        }
    }
}
