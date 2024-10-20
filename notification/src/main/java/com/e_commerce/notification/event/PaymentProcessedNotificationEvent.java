package com.e_commerce.notification.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentProcessedNotificationEvent {
    private String orderId;
    private String customerId;
    private boolean success;
}