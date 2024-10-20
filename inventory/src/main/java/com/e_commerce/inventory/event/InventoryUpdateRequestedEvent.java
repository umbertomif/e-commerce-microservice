package com.e_commerce.inventory.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryUpdateRequestedEvent {
    private String orderId;
    private String customerId;
    private String productId;
    private int quantity;
}