package com.e_commerce.inventory.config;

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

    @Value("${inventory.queue.updateRequested}")
    private String inventoryUpdateRequestedQueue;

    @Value("${orchestration.exchange}")
    private String orchestrationExchange;

    @Bean
    public Queue inventoryUpdateRequestedQueue() {
        return new Queue(inventoryUpdateRequestedQueue, true);
    }

    @Bean
    public TopicExchange orchestrationExchange() {
        return new TopicExchange(orchestrationExchange, true, false);
    }

    @Bean
    public Binding bindingInventoryUpdateRequested() {
        return BindingBuilder.bind(inventoryUpdateRequestedQueue()).to(orchestrationExchange()).with("inventory.updateRequested");
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