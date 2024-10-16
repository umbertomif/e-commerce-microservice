package com.e_commerce.orchestration.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingRequestedEvent {
    private String orderId;
    private boolean success;
}