package com.dansmultipro.ops.service;

import com.dansmultipro.ops.dto.payment.PageResponse;
import org.springframework.data.domain.Page;

import com.dansmultipro.ops.constant.StatusTypeConstant;
import com.dansmultipro.ops.dto.common.ApiPostResponseDto;
import com.dansmultipro.ops.dto.common.ApiPutResponseDto;
import com.dansmultipro.ops.dto.payment.PaymentCreateRequestDto;
import com.dansmultipro.ops.dto.payment.PaymentStatusUpdateRequestDto;
import com.dansmultipro.ops.dto.payment.PaymentResponseDto;

public interface PaymentService {

    ApiPostResponseDto create(PaymentCreateRequestDto request);

    ApiPutResponseDto updateStatus(String id, String status, PaymentStatusUpdateRequestDto request);

    PageResponse<?> getAll(StatusTypeConstant status, int page, int size, String sortBy, String sortDirection);

    PaymentResponseDto getById(String id);
}
