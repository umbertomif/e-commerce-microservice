package com.e_commerce.orchestration.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryUpdateRequestedEvent {
    private String orderId;
    private String productId;
    private int quantity;
    private boolean success;
}
