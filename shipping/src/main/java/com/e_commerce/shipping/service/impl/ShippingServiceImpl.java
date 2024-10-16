package com.e_commerce.shipping.service.impl;

import com.e_commerce.shipping.event.ShippingRequestedEvent;
import com.e_commerce.shipping.event.OrderShippedEvent;
import com.e_commerce.shipping.service.ShippingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class ShippingServiceImpl implements ShippingService {

    private static final Logger logger = LoggerFactory.getLogger(ShippingServiceImpl.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${orchestration.exchange}")
    private String orchestrationExchange;

    @Autowired
    public ShippingServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    @RabbitListener(queues = "${shipping.queue.requested}")
    public void processShipping(ShippingRequestedEvent event) {
        logger.info("Received ShippingRequestedEvent for orderId: {}", event.getOrderId());
        // Simulate Shipping Process
        boolean shippingSuccessful = simulateShippingProcess(event.getOrderId());
        // Send to Orchestration Service
        OrderShippedEvent orderShippedEvent = new OrderShippedEvent(event.getOrderId(), shippingSuccessful);
        rabbitTemplate.convertAndSend(orchestrationExchange, "order.shipped", orderShippedEvent);
        if (shippingSuccessful) {
            logger.info("Order shipped successfully for orderId: {}", event.getOrderId());
        } else {
            logger.error("Order shipping failed for orderId: {}", event.getOrderId());
        }
    }

    private boolean simulateShippingProcess(String orderId) {
        return true;
    }
}