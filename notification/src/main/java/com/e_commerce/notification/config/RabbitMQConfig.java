package com.e_commerce.notification.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue orderCreatedNotificationQueue() {
        return new Queue("orderCreatedNotificationQueue", true); // Fila durável
    }

    @Bean
    public Queue paymentProcessedNotificationQueue() {
        return new Queue("paymentProcessedNotificationQueue", true);
    }

    @Bean
    public Queue inventoryUpdatedNotificationQueue() {
        return new Queue("inventoryUpdatedNotificationQueue", true);
    }

    @Bean
    public Queue orderShippedNotificationQueue() {
        return new Queue("orderShippedNotificationQueue", true);
    }

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange("notificationExchange", true, false); // Exchange durável
    }

    @Bean
    public Binding bindingOrderCreatedNotification() {
        return BindingBuilder.bind(orderCreatedNotificationQueue()).to(notificationExchange()).with("order.created.notification");
    }

    @Bean
    public Binding bindingPaymentProcessedNotification() {
        return BindingBuilder.bind(paymentProcessedNotificationQueue()).to(notificationExchange()).with("payment.processed.notification");
    }

    @Bean
    public Binding bindingInventoryUpdatedNotification() {
        return BindingBuilder.bind(inventoryUpdatedNotificationQueue()).to(notificationExchange()).with("inventory.updated.notification");
    }

    @Bean
    public Binding bindingOrderShippedNotification() {
        return BindingBuilder.bind(orderShippedNotificationQueue()).to(notificationExchange()).with("order.shipped.notification");
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