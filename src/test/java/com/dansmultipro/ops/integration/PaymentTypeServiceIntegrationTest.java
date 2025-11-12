package com.dansmultipro.ops.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dansmultipro.ops.dto.master.PaymentTypeResponseDto;
import com.dansmultipro.ops.exception.ResourceNotFoundException;
import com.dansmultipro.ops.service.PaymentTypeService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PaymentTypeServiceIntegrationTest extends AbstractServiceIntegrationTest {

    @Autowired
    private PaymentTypeService paymentTypeService;

    @Test
    void findAllShouldIncludeDefaultPaymentType() {
        List<PaymentTypeResponseDto> responses = paymentTypeService.findAll();

        assertThat(responses)
                .extracting(PaymentTypeResponseDto::code)
                .contains(defaultPaymentType.getCode());
    }

    @Test
    void findByIdShouldReturnPaymentType() {
        PaymentTypeResponseDto response = paymentTypeService.findById(defaultPaymentType.getId().toString());

        assertThat(response.code()).isEqualTo(defaultPaymentType.getCode());
        assertThat(response.name()).isEqualTo(defaultPaymentType.getName());
    }

    @Test
    void findByIdShouldThrowWhenNotFound() {
        assertThatThrownBy(() -> paymentTypeService.findById(UUID.randomUUID().toString()))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

