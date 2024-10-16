package com.e_commerce.notification.service;

import com.e_commerce.notification.event.InventoryUpdatedNotificationEvent;
import com.e_commerce.notification.event.OrderCreatedNotificationEvent;
import com.e_commerce.notification.event.OrderShippedNotificationEvent;
import com.e_commerce.notification.event.PaymentProcessedNotificationEvent;

public interface NotificationService {

    void handleOrderCreatedNotificationEvent(OrderCreatedNotificationEvent event);

    void handlePaymentProcessedNotificationEvent(PaymentProcessedNotificationEvent event);

    void handleInventoryUpdatedNotificationEvent(InventoryUpdatedNotificationEvent event);

    void handleOrderShippedNotificationEvent(OrderShippedNotificationEvent event);
}