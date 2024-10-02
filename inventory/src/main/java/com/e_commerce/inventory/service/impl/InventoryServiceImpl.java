package com.e_commerce.inventory.service.impl;

import com.e_commerce.inventory.event.InventoryUpdateRequestedEvent;
import com.e_commerce.inventory.event.InventoryUpdatedEvent;
import com.e_commerce.inventory.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class InventoryServiceImpl implements InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${orchestration.exchange}")
    private String orchestrationExchange;

    public InventoryServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "${inventory.queue.updateRequested}")
    @Override
    public void updateInventory(InventoryUpdateRequestedEvent event) {
        logger.info("Received InventoryUpdateRequestedEvent for orderId: {}, productId: {}, quantity: {}",
                event.getOrderId(), event.getProductId(), event.getQuantity());
        // Simulate Inventory Update
        boolean updateSuccessful = simulateInventoryUpdate(event.getProductId(), event.getQuantity());
        // Send to Orchestration Service
        InventoryUpdatedEvent inventoryUpdatedEvent = new InventoryUpdatedEvent(event.getOrderId(), updateSuccessful);
        rabbitTemplate.convertAndSend(orchestrationExchange, "inventory.updated", inventoryUpdatedEvent);
        if (updateSuccessful) {
            logger.info("Inventory updated successfully for orderId: {}", event.getOrderId());
        } else {
            logger.error("Inventory update failed for orderId: {}", event.getOrderId());
        }
    }

    private boolean simulateInventoryUpdate(String productId, int quantity) {
        return quantity > 0;
    }
}
