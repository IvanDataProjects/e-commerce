package com.gestor.e_commerce.messaging.producer;

import com.gestor.e_commerce.model.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    // KafkaTemplate is the client used to send messages to Kafka
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Method called when an order is created
    public void sendOrderCreated() {
        send("ORDER_CREATED");
    }

    // Generic method to send any event to Kafka
    public void send(String event) {
        kafkaTemplate.send("orders-topic", event);
    }

    // Method called when an order is deleted
    public void sendOrderDeleted(Order order) {
        send("ORDER_DELETED:" + order.getId());
    }
}
