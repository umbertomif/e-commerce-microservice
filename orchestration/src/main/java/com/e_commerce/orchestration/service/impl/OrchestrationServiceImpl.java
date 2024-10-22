package com.e_commerce.orchestration.service.impl;

import com.e_commerce.orchestration.event.*;
import com.e_commerce.orchestration.service.OrchestrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class OrchestrationServiceImpl implements OrchestrationService {

    private static final Logger logger = LoggerFactory.getLogger(OrchestrationServiceImpl.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${orchestration.exchange}")
    private String orchestrationExchange;

    @Value("${notification.exchange}")
    private String notificationExchange;

    @Autowired
    public OrchestrationServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    @RabbitListener(queues = "${orchestration.queue.orderCreated}")
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        if (event.isSuccess()) {
            logger.info("Handling OrderCreatedEvent for orderId: {}, customerId: {}", event.getOrderId(), event.getCustomerId());
            // Send Notification
            sendOrderCreatedNotification(event);
            // Trigger the Payment request
            triggerPaymentRequest(event);
        } else {
            logger.error("Order failed for orderId: {}, customerId: {}", event.getOrderId(), event.getCustomerId());
            // Send Failed Notification
            sendOrderFailedNotification(event);
        }
    }

    private void sendOrderCreatedNotification(OrderCreatedEvent event) {
        OrderCreatedNotificationEvent notificationEvent = new OrderCreatedNotificationEvent(event.getOrderId(), event.getCustomerId(),true);
        rabbitTemplate.convertAndSend(notificationExchange, "order.created.notification", notificationEvent);
    }

    private void triggerPaymentRequest(OrderCreatedEvent event) {
        PaymentRequestedEvent paymentEvent = new PaymentRequestedEvent(event.getOrderId(), event.getCustomerId(), event.getProductId(), event.getQuantity(), calculateAmount(event), true);
        logger.info("Trigger Payment Requested event: {}", paymentEvent);
        rabbitTemplate.convertAndSend(orchestrationExchange, "payment.requested", paymentEvent);
    }

    private double calculateAmount(OrderCreatedEvent event) {
        return event.getQuantity() * 100.0;
    }

    private void sendOrderFailedNotification(OrderCreatedEvent event) {
        OrderCreatedNotificationEvent notificationEvent = new OrderCreatedNotificationEvent(event.getOrderId(), event.getCustomerId(), false);
        rabbitTemplate.convertAndSend(notificationExchange, "order.created.notification", notificationEvent);
    }

    @Override
    @RabbitListener(queues = "${orchestration.queue.paymentProcessed}")
    public void handlePaymentProcessedEvent(PaymentProcessedEvent event) {
        if (event.isSuccess()) {
            logger.info("Handling Payment processed successfully for orderId: {}, customerId: {}", event.getOrderId(), event.getCustomerId());
            // Send Notification
            sendPaymentProcessedNotification(event);
            // Trigger Update Inventory request
            triggerInventoryUpdate(event);
        } else {
            logger.error("Payment failed for orderId: {}, customerId: {}", event.getOrderId(), event.getCustomerId());
            // Send Failed Notification
            sendPaymentFailedNotification(event);
        }
    }

    private void sendPaymentProcessedNotification(PaymentProcessedEvent event) {
        PaymentProcessedNotificationEvent notificationEvent = new PaymentProcessedNotificationEvent(event.getOrderId(), event.getCustomerId(), true);
        rabbitTemplate.convertAndSend(notificationExchange, "payment.processed.notification", notificationEvent);
    }

    private void triggerInventoryUpdate(PaymentProcessedEvent event) {
        InventoryUpdateRequestedEvent inventoryEvent = new InventoryUpdateRequestedEvent(event.getOrderId(), event.getCustomerId(), event.getProductId(), event.getQuantity(), true);
        logger.info("Trigger Inventory Update Requested event: {}", inventoryEvent);
        rabbitTemplate.convertAndSend(orchestrationExchange, "inventory.updateRequested", inventoryEvent);
    }

    private void sendPaymentFailedNotification(PaymentProcessedEvent event) {
        PaymentProcessedNotificationEvent notificationEvent = new PaymentProcessedNotificationEvent(event.getOrderId(), event.getCustomerId(), false);
        rabbitTemplate.convertAndSend(notificationExchange, "payment.processed.notification", notificationEvent);
    }

    @Override
    @RabbitListener(queues = "${orchestration.queue.inventoryUpdated}")
    public void handleInventoryUpdatedEvent(InventoryUpdatedEvent event) {
        if (event.isSuccess()) {
            logger.info("Handling Inventory updated successfully for orderId: {}, customerId: {}", event.getOrderId(), event.getCustomerId());
            // Send Notification
            sendInventoryUpdatedNotification(event);
            // Trigger Shipping request
            triggerShippingRequest(event);
        } else {
            logger.error("Inventory update failed for orderId: {}, customerId: {}", event.getOrderId(), event.getCustomerId());
            // Send Failed Notification
            sendInventoryUpdateFailedNotification(event);
        }
    }

    private void sendInventoryUpdatedNotification(InventoryUpdatedEvent event) {
        InventoryUpdatedNotificationEvent notificationEvent = new InventoryUpdatedNotificationEvent(event.getOrderId(),event.getCustomerId(), true);
        rabbitTemplate.convertAndSend(notificationExchange, "inventory.updated.notification", notificationEvent);
    }

    private void triggerShippingRequest(InventoryUpdatedEvent event) {
        ShippingRequestedEvent shippingEvent = new ShippingRequestedEvent(event.getOrderId(), event.getCustomerId(), true);
        logger.info("Trigger Shipping Requested event: {}", shippingEvent);
        rabbitTemplate.convertAndSend(orchestrationExchange, "shipping.requested", shippingEvent);
    }

    private void sendInventoryUpdateFailedNotification(InventoryUpdatedEvent event) {
        InventoryUpdatedNotificationEvent notificationEvent = new InventoryUpdatedNotificationEvent(event.getOrderId(), event.getCustomerId(), false);
        rabbitTemplate.convertAndSend(notificationExchange, "inventory.updated.notification", notificationEvent);
    }

    @Override
    @RabbitListener(queues = "${orchestration.queue.orderShipped}")
    public void handleOrderShippedEvent(OrderShippedEvent event) {
        if (event.isSuccess()) {
            logger.info("Handling Order shipped successfully for orderId: {}, customerId: {}", event.getOrderId(), event.getCustomerId());
            // Send Notification
            sendOrderShippedNotification(event);
        } else {
            logger.error("Order shipping failed for orderId: {}, customerId: {}", event.getOrderId(), event.getCustomerId());
            // Send Failed Notification
            sendOrderShippedFailedNotification(event);
        }
    }

    private void sendOrderShippedNotification(OrderShippedEvent event) {
        OrderShippedNotificationEvent notificationEvent = new OrderShippedNotificationEvent(event.getOrderId(), event.getCustomerId(), true);
        rabbitTemplate.convertAndSend(notificationExchange, "order.shipped.notification", notificationEvent);
    }

    private void sendOrderShippedFailedNotification(OrderShippedEvent event) {
        OrderShippedNotificationEvent notificationEvent = new OrderShippedNotificationEvent(event.getOrderId(), event.getCustomerId(), false);
        rabbitTemplate.convertAndSend(notificationExchange, "order.shipped.notification", notificationEvent);
    }
}