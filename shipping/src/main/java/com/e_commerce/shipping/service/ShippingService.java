package com.e_commerce.shipping.service;

import com.e_commerce.shipping.event.ShippingRequestedEvent;

public interface ShippingService {

    void processShipping(ShippingRequestedEvent event);
}