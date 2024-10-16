package com.e_commerce.payment.config;

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

    @Value("${payment.queue.requested}")
    private String paymentRequestedQueue;

    @Value("${orchestration.exchange}")
    private String orchestrationExchange;

    @Bean
    public Queue paymentRequestedQueue() {
        return new Queue(paymentRequestedQueue, true);
    }

    @Bean
    public TopicExchange orchestrationExchange() {
        return new TopicExchange(orchestrationExchange, true, false);
    }

    @Bean
    public Binding bindingPaymentRequested() {
        return BindingBuilder.bind(paymentRequestedQueue()).to(orchestrationExchange()).with("payment.requested");
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