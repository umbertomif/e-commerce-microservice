package com.e_commerce.shipping.config;

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

    @Value("${shipping.queue.requested}")
    private String shippingRequestedQueue;

    @Value("${orchestration.exchange}")
    private String orchestrationExchange;

    @Bean
    public Queue shippingRequestedQueue() {
        return new Queue(shippingRequestedQueue, true);
    }

    @Bean
    public TopicExchange orchestrationExchange() {
        return new TopicExchange(orchestrationExchange, true, false);
    }

    @Bean
    public Binding bindingShippingRequested() {
        return BindingBuilder.bind(shippingRequestedQueue()).to(orchestrationExchange()).with("shipping.requested");
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
