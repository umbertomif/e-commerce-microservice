package com.e_commerce.payment.service.impl;

import com.e_commerce.payment.event.PaymentRequestedEvent;
import com.e_commerce.payment.event.PaymentProcessedEvent;
import com.e_commerce.payment.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);
    private final RabbitTemplate rabbitTemplate;

    @Value("${orchestration.exchange}")
    private String orchestrationExchange;

    @Autowired
    public PaymentServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    @RabbitListener(queues = "${payment.queue.requested}")
    public void processPayment(PaymentRequestedEvent event) {
        logger.info("Received PaymentRequestedEvent for orderId: {}", event.getOrderId());
        // Simulate the Payment processed
        boolean paymentSuccessful = simulatePaymentProcess(event.getAmount());
        // Send to Orchestration Service
        PaymentProcessedEvent paymentProcessedEvent = new PaymentProcessedEvent(event.getOrderId(), paymentSuccessful);
        rabbitTemplate.convertAndSend(orchestrationExchange, "payment.processed", paymentProcessedEvent);
        if (paymentSuccessful) {
            logger.info("Payment processed successfully for orderId: {}", event.getOrderId());
        } else {
            logger.error("Payment failed for orderId: {}", event.getOrderId());
        }
    }

    private boolean simulatePaymentProcess(double amount) {
        return amount > 0;
    }
}
