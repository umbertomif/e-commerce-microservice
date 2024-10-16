package com.e_commerce.orchestration.service;

import com.e_commerce.orchestration.event.*;

public interface OrchestrationService {

    void handleOrderCreatedEvent(OrderCreatedEvent event);

    void handlePaymentProcessedEvent(PaymentProcessedEvent event);

    void handleInventoryUpdatedEvent(InventoryUpdatedEvent event);

    void handleOrderShippedEvent(OrderShippedEvent event);
}
