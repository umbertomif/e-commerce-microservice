package com.e_commerce.notification.service.impl;

import com.e_commerce.notification.event.*;
import com.e_commerce.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @RabbitListener(queues = "${notification.queue.orderCreated}")
    public void handleOrderCreatedNotificationEvent(OrderCreatedNotificationEvent event) {
        if (event.isSuccess()) {
            logger.info("Order Created Notification sent for orderId: {}", event.getOrderId());
        } else {
            logger.error("Order Creation Failed Notification sent for orderId: {}", event.getOrderId());
        }
    }

    @RabbitListener(queues = "${notification.queue.paymentProcessed}")
    public void handlePaymentProcessedNotificationEvent(PaymentProcessedNotificationEvent event) {
        if (event.isSuccess()) {
            logger.info("Payment Processed Notification sent for orderId: {}", event.getOrderId());
        } else {
            logger.error("Payment Failed Notification sent for orderId: {}", event.getOrderId());
        }
    }

    @RabbitListener(queues = "${notification.queue.inventoryUpdated}")
    public void handleInventoryUpdatedNotificationEvent(InventoryUpdatedNotificationEvent event) {
        if (event.isSuccess()) {
            logger.info("Inventory Updated Notification sent for orderId: {}", event.getOrderId());
        } else {
            logger.error("Inventory Update Failed Notification sent for orderId: {}", event.getOrderId());
        }
    }

    @RabbitListener(queues = "${notification.queue.orderShipped}")
    public void handleOrderShippedNotificationEvent(OrderShippedNotificationEvent event) {
        if (event.isSuccess()) {
            logger.info("Order Shipped Notification sent for orderId: {}", event.getOrderId());
        } else {
            logger.error("Order Shipping Failed Notification sent for orderId: {}", event.getOrderId());
        }
    }
}