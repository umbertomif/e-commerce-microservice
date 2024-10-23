package com.e_commerce.order.service.impl;

import com.e_commerce.order.dto.OrderDTO;
import com.e_commerce.order.event.OrderCreatedEvent;
import com.e_commerce.order.service.OrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${orchestration.exchange}")
    private String orchestrationExchange;

    @Autowired
    public OrderServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public OrderCreatedEvent createOrder(OrderDTO orderDTO) {
        // Generate a unique order ID
        String orderId = UUID.randomUUID().toString();
        // Create an OrderCreatedEvent to send to RabbitMQ
        OrderCreatedEvent event = new OrderCreatedEvent(orderId, orderDTO.getCustomerId(), orderDTO.getProductId(), orderDTO.getQuantity(), true);
        // Trigger the Order Created Event
        triggerOrderCreated(event);
        // Return the OrderCreatedEvent
        return event;
    }

    private void triggerOrderCreated(OrderCreatedEvent event) {
        rabbitTemplate.convertAndSend(orchestrationExchange, "order.created", event);
        logger.info("Order Created successfully for orderId: {}, customerId: {}", event.getOrderId(), event.getCustomerId());
    }
}