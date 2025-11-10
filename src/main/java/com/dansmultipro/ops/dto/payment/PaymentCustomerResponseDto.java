package com.dansmultipro.ops.dto.payment;

import java.math.BigDecimal;

public record PaymentCustomerResponseDto(
        String id,
        String productType,
        String paymentType,
        String customerNumber,
        BigDecimal amount,
        String status,
        String referenceNo) {
}
