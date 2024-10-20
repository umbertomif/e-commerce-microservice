package com.e_commerce.orchestration.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestedEvent {
    private String orderId;
    private String customerId;
    private String productId;
    private int quantity;
    private double amount;
    private boolean success;
}