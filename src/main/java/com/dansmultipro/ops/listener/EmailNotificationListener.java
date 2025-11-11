package com.dansmultipro.ops.listener;

import com.dansmultipro.ops.config.RabbitConfig;
import com.dansmultipro.ops.dto.notification.EmailNotificationMessageDto;
import com.dansmultipro.ops.util.EmailUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationListener {

    private final EmailUtil emailUtil;

    public EmailNotificationListener(EmailUtil emailUtil) {
        this.emailUtil = emailUtil;
    }

    @RabbitListener(queues = RabbitConfig.PAYMENT_GATEWAY_NOTIFICATION_QUEUE)
    public void handleCreationNotification(EmailNotificationMessageDto message) {
        sendEmail(message);
    }

    @RabbitListener(queues = RabbitConfig.PAYMENT_CUSTOMER_NOTIFICATION_QUEUE)
    public void handleUpdateNotification(EmailNotificationMessageDto message) {
        sendEmail(message);
    }

    @RabbitListener(queues = RabbitConfig.PAYMENT_GATEWAY_REPLY_QUEUE)
    public void handleReplyNotification(EmailNotificationMessageDto message) {
        sendEmail(message);
    }

    private void sendEmail(EmailNotificationMessageDto message) {
        emailUtil.send(message.email(), message.subject(), message.body());
    }
}