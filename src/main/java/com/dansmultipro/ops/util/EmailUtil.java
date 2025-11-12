package com.dansmultipro.ops.util;

import com.dansmultipro.ops.constant.StatusTypeConstant;
import com.dansmultipro.ops.dto.notification.PaymentEmailPayload;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

    private final JavaMailSender mailSender;

    public EmailUtil(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public String buildGatewayCreationSubject(PaymentEmailPayload payload) {
        return "[OPS] Payment Created - %s".formatted(payload.paymentId());
    }

    public String buildGatewayCreationBody(PaymentEmailPayload payload) {
        return "A new payment has been created with ID %s for customer %s (%s).".formatted(
                payload.paymentId(),
                payload.customerFullName(),
                payload.customerNumber());
    }

    public String buildCustomerStatusSubject(PaymentEmailPayload payload) {
        return "[OPS] Payment %s - %s".formatted(payload.paymentId(), payload.statusCode());
    }

    public String buildCustomerStatusBody(PaymentEmailPayload payload) {
        String statusCode = payload.statusCode();
        String base = "Your payment %s for customer number %s is %s.".formatted(
                payload.paymentId(),
                payload.customerNumber(),
                statusCode.toLowerCase());
        if (StatusTypeConstant.REJECTED.name().equalsIgnoreCase(statusCode) && payload.gatewayNote() != null) {
            return base + " Gateway note: " + payload.gatewayNote();
        }
        if (StatusTypeConstant.APPROVED.name().equalsIgnoreCase(statusCode) && payload.referenceNo() != null) {
            return base + " Reference number: " + payload.referenceNo();
        }
        return base;
    }

    public String buildForgotPasswordSubject() {
        return "[OPS] Temporary Password";
    }

    public String buildForgotPasswordBody(String temporaryPassword) {
        return "Your temporary password is: %s. Please log in and change it immediately.".formatted(temporaryPassword);
    }
}
