package com.e_commerce.payment.service;

import com.e_commerce.payment.event.PaymentRequestedEvent;

public interface PaymentService {
    void processPayment(PaymentRequestedEvent event);
}