package com.e_commerce.orchestration.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${orchestration.queue.orderCreated}")
    private String orderCreatedQueue;

    @Value("${orchestration.queue.paymentProcessed}")
    private String paymentProcessedQueue;

    @Value("${orchestration.queue.inventoryUpdated}")
    private String inventoryUpdatedQueue;

    @Value("${orchestration.queue.orderShipped}")
    private String orderShippedQueue;

    @Value("${notification.exchange}")
    private String notificationExchange;

    @Value("${orchestration.exchange}")
    private String orchestrationExchange;

    // Queues
    @Bean
    public Queue orderCreatedQueue() {
        return new Queue(orderCreatedQueue, true);
    }

    @Bean
    public Queue paymentProcessedQueue() {
        return new Queue(paymentProcessedQueue, true);
    }

    @Bean
    public Queue inventoryUpdatedQueue() {
        return new Queue(inventoryUpdatedQueue, true);
    }

    @Bean
    public Queue orderShippedQueue() {
        return new Queue(orderShippedQueue, true);
    }

    // Exchanges
    @Bean
    public TopicExchange orchestrationExchange() {
        return new TopicExchange(orchestrationExchange, true, false);
    }

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(notificationExchange, true, false);
    }

    // Bindings
    @Bean
    public Binding bindingOrderCreated() {
        return BindingBuilder.bind(orderCreatedQueue()).to(orchestrationExchange()).with("order.created");
    }

    @Bean
    public Binding bindingPaymentProcessed() {
        return BindingBuilder.bind(paymentProcessedQueue()).to(orchestrationExchange()).with("payment.processed");
    }

    @Bean
    public Binding bindingInventoryUpdated() {
        return BindingBuilder.bind(inventoryUpdatedQueue()).to(orchestrationExchange()).with("inventory.updated");
    }

    @Bean
    public Binding bindingOrderShipped() {
        return BindingBuilder.bind(orderShippedQueue()).to(orchestrationExchange()).with("order.shipped");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}