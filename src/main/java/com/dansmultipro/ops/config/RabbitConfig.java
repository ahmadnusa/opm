package com.dansmultipro.ops.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

    public static final String PAYMENT_NOTIFICATION_EXCHANGE = "payment.notification.exchange";

    public static final String PAYMENT_GATEWAY_NOTIFICATION_QUEUE = "payment.notification.queue.gateway";
    public static final String PAYMENT_GATEWAY_NOTIFICATION_ROUTING_KEY = "payment.notification.routing-key.gateway";

    public static final String PAYMENT_CUSTOMER_NOTIFICATION_QUEUE = "payment.notification.queue.customer";
    public static final String PAYMENT_CUSTOMER_NOTIFICATION_ROUTING_KEY = "payment.notification.routing-key.customer";

    public static final String FORGOT_PASSWORD_NOTIFICATION_QUEUE = "user.notification.queue.forgot-password";
    public static final String FORGOT_PASSWORD_NOTIFICATION_ROUTING_KEY = "user.notification.routing-key.forgot-password";

    @Bean
    public DirectExchange paymentNotificationExchange() {
        return new DirectExchange(PAYMENT_NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Queue gatewayNotificationQueue() {
        return QueueBuilder.durable(PAYMENT_GATEWAY_NOTIFICATION_QUEUE).build();
    }

    @Bean
    public Queue customerNotificationQueue() {
        return QueueBuilder.durable(PAYMENT_CUSTOMER_NOTIFICATION_QUEUE).build();
    }

    @Bean
    public Queue forgotPasswordQueue() {
        return QueueBuilder.durable(FORGOT_PASSWORD_NOTIFICATION_QUEUE).build();
    }

    @Bean
    public Binding gatewayNotificationBinding() {
        return BindingBuilder.bind(gatewayNotificationQueue())
                .to(paymentNotificationExchange())
                .with(PAYMENT_GATEWAY_NOTIFICATION_ROUTING_KEY);
    }

    @Bean
    public Binding customerNotificationBinding() {
        return BindingBuilder.bind(customerNotificationQueue())
                .to(paymentNotificationExchange())
                .with(PAYMENT_CUSTOMER_NOTIFICATION_ROUTING_KEY);
    }

    @Bean
    public Binding forgotPasswordBinding() {
        return BindingBuilder.bind(forgotPasswordQueue())
                .to(paymentNotificationExchange())
                .with(FORGOT_PASSWORD_NOTIFICATION_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
