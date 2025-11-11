package com.dansmultipro.ops.dto.notification;

public record EmailNotificationMessageDto(
        String email,
        String subject,
        String body) {
}
