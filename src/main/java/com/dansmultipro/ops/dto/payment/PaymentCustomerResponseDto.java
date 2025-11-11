package com.dansmultipro.ops.dto.payment;

public record PaymentCustomerResponseDto(
        String id,
        String productType,
        String paymentType,
        String customerNumber,
        String amount,
        String status,
        String referenceNo) {
}
