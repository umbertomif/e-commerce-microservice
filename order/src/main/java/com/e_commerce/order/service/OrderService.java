package com.e_commerce.order.service;

import com.e_commerce.order.dto.OrderDTO;
import com.e_commerce.order.model.Order;

public interface OrderService {

    Order createOrder(OrderDTO orderDTO);

    void compensateOrder(String orderId);
}
