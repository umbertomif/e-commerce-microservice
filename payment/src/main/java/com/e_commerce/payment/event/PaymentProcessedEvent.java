package com.e_commerce.payment.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentProcessedEvent {
    private String orderId;
    private String customerId;
    private String productId;
    private int quantity;
    private boolean success;
}