package com.e_commerce.orchestration.service.impl;

import com.e_commerce.orchestration.event.*;
import com.e_commerce.orchestration.service.OrchestrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class OrchestrationServiceImpl implements OrchestrationService {

    private static final Logger logger = LoggerFactory.getLogger(OrchestrationServiceImpl.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${notification.exchange}")
    private String notificationExchange;

    public OrchestrationServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    @RabbitListener(queues = "${orchestration.queue.orderCreated}")
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        if (event.isSuccess()) {
            logger.info("Handling OrderCreatedEvent for orderId: {}", event.getOrderId());
            // Send Notification
            OrderCreatedNotificationEvent notificationEvent = new OrderCreatedNotificationEvent(event.getOrderId(), true);
            rabbitTemplate.convertAndSend(notificationExchange, "order.created.notification", notificationEvent);
            // Trigger the Payment request
            PaymentRequestedEvent paymentEvent = new PaymentRequestedEvent(event.getOrderId(), calculateAmount(event), true);
            rabbitTemplate.convertAndSend("orchestrationExchange", "payment.requested", paymentEvent);
        } else {
            logger.error("Order failed for orderId: {}", event.getOrderId());
            // Send Failed Notification
            OrderCreatedNotificationEvent notificationEvent = new OrderCreatedNotificationEvent(event.getOrderId(), false);
            rabbitTemplate.convertAndSend(notificationExchange, "order.created.notification", notificationEvent);
            // Compensate Order
            compensateOrder(event.getOrderId());
        }
    }

    @Override
    @RabbitListener(queues = "${orchestration.queue.paymentProcessed}")
    public void handlePaymentProcessedEvent(PaymentProcessedEvent event) {
        if (event.isSuccess()) {
            logger.info("Payment processed successfully for orderId: {}", event.getOrderId());
            // Send Notification
            PaymentProcessedNotificationEvent notificationEvent = new PaymentProcessedNotificationEvent(event.getOrderId(), true);
            rabbitTemplate.convertAndSend(notificationExchange, "payment.processed.notification", notificationEvent);
            // Trigger Update Inventory request
            InventoryUpdateRequestedEvent inventoryEvent = new InventoryUpdateRequestedEvent(event.getOrderId(), "someProductId", 10, true);
            rabbitTemplate.convertAndSend("orchestrationExchange", "inventory.updateRequested", inventoryEvent);
        } else {
            logger.error("Payment failed for orderId: {}", event.getOrderId());
            // Send Failed Notification
            PaymentProcessedNotificationEvent notificationEvent = new PaymentProcessedNotificationEvent(event.getOrderId(), false);
            rabbitTemplate.convertAndSend(notificationExchange, "payment.processed.notification", notificationEvent);
            // Compensate Order
            compensateOrder(event.getOrderId());
        }
    }

    @Override
    @RabbitListener(queues = "${orchestration.queue.inventoryUpdated}")
    public void handleInventoryUpdatedEvent(InventoryUpdatedEvent event) {
        if (event.isSuccess()) {
            logger.info("Inventory updated successfully for orderId: {}", event.getOrderId());
            // Send Notification
            InventoryUpdatedNotificationEvent notificationEvent = new InventoryUpdatedNotificationEvent(event.getOrderId(), true);
            rabbitTemplate.convertAndSend(notificationExchange, "inventory.updated.notification", notificationEvent);
            // Trigger Shipping request
            ShippingRequestedEvent shippingEvent = new ShippingRequestedEvent(event.getOrderId(), true);
            rabbitTemplate.convertAndSend("orchestrationExchange", "shipping.requested", shippingEvent);
        } else {
            logger.error("Inventory update failed for orderId: {}", event.getOrderId());
            // Send Failed Notification
            InventoryUpdatedNotificationEvent notificationEvent = new InventoryUpdatedNotificationEvent(event.getOrderId(), false);
            rabbitTemplate.convertAndSend(notificationExchange, "inventory.updated.notification", notificationEvent);
            // Compensate Order
            compensateOrder(event.getOrderId());
        }
    }

    @Override
    @RabbitListener(queues = "${orchestration.queue.orderShipped}")
    public void handleOrderShippedEvent(OrderShippedEvent event) {
        if (event.isSuccess()) {
            logger.info("Order shipped successfully for orderId: {}", event.getOrderId());
            // Send Notification
            OrderShippedNotificationEvent notificationEvent = new OrderShippedNotificationEvent(event.getOrderId(), true);
            rabbitTemplate.convertAndSend(notificationExchange, "order.shipped.notification", notificationEvent);
        } else {
            logger.error("Order shipping failed for orderId: {}", event.getOrderId());
            // Send Failed Notification
            OrderShippedNotificationEvent notificationEvent = new OrderShippedNotificationEvent(event.getOrderId(), false);
            rabbitTemplate.convertAndSend(notificationExchange, "order.shipped.notification", notificationEvent);
            // Compensate Order
            compensateOrder(event.getOrderId());
        }
    }

    private double calculateAmount(OrderCreatedEvent event) {
        return event.getQuantity() * 100.0;
    }

    private void compensateOrder(String orderId) {
        logger.info("Compensating order: {}", orderId);
        rabbitTemplate.convertAndSend("orchestrationExchange", "order.compensated", orderId);
    }
}