package com.e_commerce.notification.service;

import com.e_commerce.notification.event.NotificationEvent;

public interface NotificationService {

    void handleOrderCreatedNotificationEvent(NotificationEvent event);

    void handlePaymentProcessedNotificationEvent(NotificationEvent event);

    void handleInventoryUpdatedNotificationEvent(NotificationEvent event);

    void handleOrderShippedNotificationEvent(NotificationEvent event);
}