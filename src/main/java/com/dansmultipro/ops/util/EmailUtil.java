package com.dansmultipro.ops.util;

import com.dansmultipro.ops.constant.StatusTypeConstant;
import com.dansmultipro.ops.model.Payment;
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

    public String buildGatewayCreationSubject(Payment payment) {
        return "[OPS] Payment Created - %s".formatted(payment.getId());
    }

    public String buildGatewayCreationBody(Payment payment) {
        return "A new payment has been created with ID %s for customer %s (%s).".formatted(
                payment.getId(),
                payment.getCustomer().getFullName(),
                payment.getCustomerNumber());
    }

    public String buildCustomerStatusSubject(Payment payment) {
        return "[OPS] Payment %s - %s".formatted(payment.getId(), payment.getStatus().getCode());
    }

    public String buildCustomerStatusBody(Payment payment) {
        String statusCode = payment.getStatus().getCode();
        String base = "Your payment %s for customer number %s is %s.".formatted(
                payment.getId(),
                payment.getCustomerNumber(),
                statusCode.toLowerCase());
        if (StatusTypeConstant.REJECTED.name().equalsIgnoreCase(statusCode) && payment.getGatewayNote() != null) {
            return base + " Gateway note: " + payment.getGatewayNote();
        }
        if (StatusTypeConstant.APPROVED.name().equalsIgnoreCase(statusCode) && payment.getReferenceNo() != null) {
            return base + " Reference number: " + payment.getReferenceNo();
        }
        return base;
    }
}