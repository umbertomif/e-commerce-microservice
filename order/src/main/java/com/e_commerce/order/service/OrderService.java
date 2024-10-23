package com.e_commerce.order.service;

import com.e_commerce.order.dto.OrderDTO;
import com.e_commerce.order.event.OrderCreatedEvent;

public interface OrderService {

    OrderCreatedEvent createOrder(OrderDTO orderDTO);
}