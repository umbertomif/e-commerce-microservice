package com.e_commerce.inventory.service;

import com.e_commerce.inventory.event.InventoryUpdateRequestedEvent;

public interface InventoryService {
    void updateInventory(InventoryUpdateRequestedEvent event);
}
