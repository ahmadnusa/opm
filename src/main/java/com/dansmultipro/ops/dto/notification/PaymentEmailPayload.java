package com.dansmultipro.ops.dto.notification;

import com.dansmultipro.ops.model.Payment;

public record PaymentEmailPayload(
                String paymentId,
                String customerFullName,
                String customerNumber,
                String statusCode,
                String gatewayNote,
                String referenceNo) {

        public static PaymentEmailPayload from(Payment payment) {
                return new PaymentEmailPayload(
                                payment.getId().toString(),
                                payment.getCustomer().getFullName(),
                                payment.getCustomerNumber(),
                                payment.getStatus().getCode(),
                                payment.getGatewayNote(),
                                payment.getReferenceNo());
        }
}

