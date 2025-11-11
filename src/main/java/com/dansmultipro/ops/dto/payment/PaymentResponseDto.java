package com.dansmultipro.ops.dto.payment;

public record PaymentResponseDto(
        String id,
        String customerId,
        String customerName,
        String productType,
        String paymentType,
        String customerNumber,
        String amount,
        String status,
        String referenceNo,
        Boolean isActive,
        Integer optLock) {
}
