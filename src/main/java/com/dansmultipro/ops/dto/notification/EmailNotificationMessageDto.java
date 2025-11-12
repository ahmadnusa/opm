package com.dansmultipro.ops.dto.notification;

public record EmailNotificationMessageDto(
                String email,
                PaymentEmailPayload payment,
                String temporaryPassword) {

        public static EmailNotificationMessageDto paymentMessage(String email, PaymentEmailPayload payload) {
                return new EmailNotificationMessageDto(email, payload, null);
        }

        public static EmailNotificationMessageDto forgotPasswordMessage(String email, String temporaryPassword) {
                return new EmailNotificationMessageDto(email, null, temporaryPassword);
        }
}
