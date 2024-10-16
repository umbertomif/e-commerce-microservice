package com.e_commerce.order.service.impl;

import com.e_commerce.order.dto.OrderDTO;
import com.e_commerce.order.model.Order;
import com.e_commerce.order.event.OrderCreatedEvent;
import com.e_commerce.order.service.OrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public OrderServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public Order createOrder(OrderDTO orderDTO) {
        // Generate a unique order ID
        String orderId = UUID.randomUUID().toString();
        // Create a new order
        Order order = new Order(orderId, "Created", orderDTO.getProductId(), orderDTO.getQuantity(), orderDTO.getCustomerId());
        // Log the order creation
        logger.info("Order created: {}", order);
        // Create an OrderCreatedEvent to send to RabbitMQ
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(orderId, orderDTO.getProductId(), orderDTO.getQuantity(), orderDTO.getCustomerId(), true);
        try {
            // Send the event to the orchestration exchange with the appropriate routing key
            rabbitTemplate.convertAndSend("orchestrationExchange", "order.created", orderCreatedEvent);
            logger.info("OrderCreated event sent for orderId: {}", orderId);
        } catch (Exception e) {
            // Log an error if there is a failure in sending the message
            logger.error("Error sending OrderCreated event for orderId: {}. Error: {}", orderId, e.getMessage());
        }
        // Return the created order
        return order;
    }
}