package com.dansmultipro.ops.listener;

import com.dansmultipro.ops.config.RabbitConfig;
import com.dansmultipro.ops.dto.notification.EmailNotificationMessageDto;
import com.dansmultipro.ops.dto.notification.PaymentEmailPayload;
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
        sendEmail(
                message.email(),
                emailUtil.buildGatewayCreationSubject(requirePaymentPayload(message)),
                emailUtil.buildGatewayCreationBody(requirePaymentPayload(message)));
    }

    @RabbitListener(queues = RabbitConfig.PAYMENT_CUSTOMER_NOTIFICATION_QUEUE)
    public void handleUpdateNotification(EmailNotificationMessageDto message) {
        sendEmail(
                message.email(),
                emailUtil.buildCustomerStatusSubject(requirePaymentPayload(message)),
                emailUtil.buildCustomerStatusBody(requirePaymentPayload(message)));
    }

    @RabbitListener(queues = RabbitConfig.FORGOT_PASSWORD_NOTIFICATION_QUEUE)
    public void handleForgotPasswordNotification(EmailNotificationMessageDto message) {
        sendEmail(
                message.email(),
                emailUtil.buildForgotPasswordSubject(),
                emailUtil.buildForgotPasswordBody(requireTemporaryPassword(message)));
    }

    private void sendEmail(String email, String subject, String body) {
        emailUtil.send(email, subject, body);
    }

    private PaymentEmailPayload requirePaymentPayload(
            EmailNotificationMessageDto message) {
        if (message.payment() == null) {
            throw new IllegalArgumentException("Payment payload is required for this notification.");
        }
        return message.payment();
    }

    private String requireTemporaryPassword(EmailNotificationMessageDto message) {
        if (message.temporaryPassword() == null || message.temporaryPassword().isBlank()) {
            throw new IllegalArgumentException("Temporary password is required for this notification.");
        }
        return message.temporaryPassword();
    }
}
