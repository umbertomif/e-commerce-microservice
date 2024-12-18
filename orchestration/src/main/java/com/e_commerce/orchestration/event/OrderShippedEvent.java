package com.e_commerce.orchestration.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderShippedEvent {
    private String orderId;
    private String customerId;
    private boolean success;
}