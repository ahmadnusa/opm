package com.dansmultipro.ops.dto.payment;

import java.math.BigDecimal;

public record PaymentCustomerResponseDto(
        String id,
        String productType,
        String paymentType,
        String customerNumber,
        String amount,
        String status,
        String referenceNo) {
}
