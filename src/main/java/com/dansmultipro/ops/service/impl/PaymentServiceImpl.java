package com.dansmultipro.ops.service.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dansmultipro.ops.constant.ResponseConstant;
import com.dansmultipro.ops.constant.RoleTypeConstant;
import com.dansmultipro.ops.constant.StatusTypeConstant;
import com.dansmultipro.ops.dto.common.ApiPostResponseDto;
import com.dansmultipro.ops.dto.common.ApiPutResponseDto;
import com.dansmultipro.ops.dto.payment.PaymentCreateRequestDto;
import com.dansmultipro.ops.dto.payment.PaymentCustomerResponseDto;
import com.dansmultipro.ops.dto.payment.PaymentResponseDto;
import com.dansmultipro.ops.dto.payment.PaymentStatusUpdateRequestDto;
import com.dansmultipro.ops.exception.BusinessRuleException;
import com.dansmultipro.ops.exception.ResourceNotFoundException;
import com.dansmultipro.ops.model.Payment;
import com.dansmultipro.ops.model.User;
import com.dansmultipro.ops.model.master.PaymentType;
import com.dansmultipro.ops.model.master.ProductType;
import com.dansmultipro.ops.model.master.StatusType;
import com.dansmultipro.ops.repository.PaymentRepo;
import com.dansmultipro.ops.repository.PaymentTypeRepo;
import com.dansmultipro.ops.repository.ProductTypeRepo;
import com.dansmultipro.ops.repository.StatusTypeRepo;
import com.dansmultipro.ops.service.PaymentService;
import com.dansmultipro.ops.spec.PaymentSpecsification;

import jakarta.transaction.Transactional;

@Service
public class PaymentServiceImpl extends BaseService implements PaymentService {

    private static final String RESOURCE_NAME = "Payment";
    private static final DateTimeFormatter REF_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final int REF_RANDOM_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final PaymentRepo paymentRepo;
    private final ProductTypeRepo productTypeRepo;
    private final PaymentTypeRepo paymentTypeRepo;
    private final StatusTypeRepo statusTypeRepo;

    public PaymentServiceImpl(
            PaymentRepo paymentRepo,
            ProductTypeRepo productTypeRepo,
            PaymentTypeRepo paymentTypeRepo,
            StatusTypeRepo statusTypeRepo) {
        this.paymentRepo = paymentRepo;
        this.productTypeRepo = productTypeRepo;
        this.paymentTypeRepo = paymentTypeRepo;
        this.statusTypeRepo = statusTypeRepo;
    }

    @Override
    @Transactional
    @CacheEvict(value = "payments", allEntries = true)
    public ApiPostResponseDto create(PaymentCreateRequestDto request) {
        User customer = fetchUser(authUtil.getLoginId());

        Payment payment = new Payment();
        payment.setCustomer(customer);
        payment.setProductType(fetchProductType(request.productType()));
        payment.setPaymentType(fetchPaymentType(request.paymentType()));
        payment.setCustomerNumber(request.customerNumber());
        payment.setAmount(request.amount());
        payment.setDescription(request.description());
        payment.setStatus(fetchStatusType(StatusTypeConstant.PROCESSING));
        payment.setGatewayNote(null);
        payment.setReferenceNo(null);

        prepareCreate(payment);
        Payment saved = paymentRepo.save(payment);

        String message = messageBuilder(RESOURCE_NAME, ResponseConstant.SAVED.getValue());
        return new ApiPostResponseDto(saved.getId().toString(), message);
    }

    @Override
    @Transactional
    @CacheEvict(value = "payments", allEntries = true)
    public ApiPutResponseDto updateStatus(String id, String status, PaymentStatusUpdateRequestDto request) {
        String normalizedStatus = status.trim().toUpperCase();

        Payment payment = fetchPayment(getUUID(id));
        ensureProcessing(payment);

        switch (normalizedStatus) {
            case "CANCELLED" -> {
                ensureCustomerRole();
                ensureOwner(payment);
                applyStatusChange(payment, StatusTypeConstant.CANCELLED, null, null);
            }
            case "APPROVED" -> {
                ensureGatewayRole();
                applyStatusChange(payment, StatusTypeConstant.APPROVED, null, generateReferenceNo());
            }
            case "REJECTED" -> {
                ensureGatewayRole();
                String gatewayNote = request == null ? null : request.gatewayNote();
                if (gatewayNote == null || gatewayNote.isBlank()) {
                    throw new BusinessRuleException("Gateway note is required when rejecting a payment.");
                }
                applyStatusChange(payment, StatusTypeConstant.REJECTED, gatewayNote, null);
            }
            default -> throw new BusinessRuleException(
                    messageBuilder("Status", ResponseConstant.INVALID_VALUE));
        }

        prepareUpdate(payment);
        Payment updated = paymentRepo.save(payment);

        String message = messageBuilder(RESOURCE_NAME, ResponseConstant.UPDATED.getValue());
        return new ApiPutResponseDto(updated.getOptLock(), message);
    }

    @Override
    @Cacheable(value = "payments", key = "#root.target.buildCacheKey(#status, #page, #size, #sortBy, #sortDirection)")
    public Page<?> getAll(StatusTypeConstant status, int page, int size, String sortBy, String sortDirection) {
        Specification<Payment> spec = Specification.allOf(
                PaymentSpecsification.byStatus(status));

        RoleTypeConstant role = authUtil.roleLogin();
        boolean isCustomer = role == RoleTypeConstant.CUSTOMER;
        if (isCustomer) {
            spec = spec.and(PaymentSpecsification.byCustomerId(authUtil.getLoginId()));
        }

        Pageable pageable = buildPageable(page, size, sortBy, sortDirection);
        Page<Payment> payments = paymentRepo.findAll(spec, pageable);
        return isCustomer
                ? payments.map(this::toCustomerDto)
                : payments.map(this::toDto);
    }

    @Override
    public PaymentResponseDto getById(String id) {
        Payment payment = fetchPayment(getUUID(id));

        return toDto(payment);
    }

    private void applyStatusChange(Payment payment, StatusTypeConstant statusType, String gatewayNote,
            String referenceNo) {
        payment.setStatus(fetchStatusType(statusType));
        payment.setGatewayNote(gatewayNote);
        payment.setReferenceNo(referenceNo);
    }

    private void ensureCustomerRole() {
        if (!authUtil.hasRole(RoleTypeConstant.CUSTOMER)) {
            throw new BusinessRuleException(messageBuilder("Access", ResponseConstant.CUSTOMER_REQUIRED));
        }
    }

    private void ensureGatewayRole() {
        if (!authUtil.hasRole(RoleTypeConstant.GATEWAY)) {
            throw new BusinessRuleException(messageBuilder("Access", ResponseConstant.GATEWAY_REQUIRED));
        }
    }

    private void ensureOwner(Payment payment) {
        UUID loginId = authUtil.getLoginId();
        UUID customerId = payment.getCustomer().getId();
        if (!loginId.equals(customerId)) {
            throw new BusinessRuleException(messageBuilder(RESOURCE_NAME, ResponseConstant.NOT_OWNER));
        }
    }

    private void ensureProcessing(Payment payment) {
        if (!StatusTypeConstant.PROCESSING.name().equals(payment.getStatus().getCode())) {
            throw new BusinessRuleException(messageBuilder(RESOURCE_NAME, ResponseConstant.NOT_PROCESSING));
        }
    }

    private Payment fetchPayment(UUID id) {
        return paymentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageBuilder(RESOURCE_NAME, ResponseConstant.NOT_FOUND)));
    }

    private User fetchUser(UUID id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageBuilder("User", ResponseConstant.NOT_FOUND)));
    }

    private ProductType fetchProductType(String code) {
        return productTypeRepo.findByCode(code)
                .orElseThrow(() -> new BusinessRuleException(
                        messageBuilder("Product type", ResponseConstant.NOT_FOUND)));
    }

    private PaymentType fetchPaymentType(String code) {
        return paymentTypeRepo.findByCode(code)
                .orElseThrow(() -> new BusinessRuleException(
                        messageBuilder("Payment type", ResponseConstant.NOT_FOUND)));
    }

    private StatusType fetchStatusType(StatusTypeConstant statusType) {
        return statusTypeRepo.findByCode(statusType.name())
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageBuilder("Status Type", ResponseConstant.NOT_FOUND)));
    }

    private String generateReferenceNo() {
        String timestamp = LocalDateTime.now().format(REF_FORMATTER);
        StringBuilder randomPart = new StringBuilder();
        for (int i = 0; i < REF_RANDOM_LENGTH; i++) {
            int value = RANDOM.nextInt(36);
            randomPart.append(Character.toUpperCase(Character.forDigit(value, 36)));
        }
        return "PGW-%s-%s".formatted(timestamp, randomPart);
    }

    private Pageable buildPageable(int page, int size, String sortBy, String sortDirection) {
        int safePage = Math.max(page, 0);
        int safeSize = size <= 0 ? 10 : size;
        String sortProperty = (sortBy == null || sortBy.isBlank()) ? "createdAt" : sortBy;
        Sort.Direction direction = "ASC".equalsIgnoreCase(sortDirection)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        return PageRequest.of(safePage, safeSize, Sort.by(direction, sortProperty));
    }

    private PaymentResponseDto toDto(Payment payment) {
        return new PaymentResponseDto(
                payment.getId().toString(),
                payment.getCustomer().getId().toString(),
                payment.getCustomer().getFullName(),
                payment.getProductType().getCode(),
                payment.getPaymentType().getCode(),
                payment.getCustomerNumber(),
                payment.getAmount(),
                payment.getStatus().getCode(),
                payment.getReferenceNo(),
                payment.getIsActive(),
                payment.getOptLock());
    }

    private PaymentCustomerResponseDto toCustomerDto(Payment payment) {
        return new PaymentCustomerResponseDto(
                payment.getId().toString(),
                payment.getProductType().getCode(),
                payment.getPaymentType().getCode(),
                payment.getCustomerNumber(),
                payment.getAmount(),
                payment.getStatus().getCode(),
                payment.getReferenceNo());
    }

    public String buildCacheKey(StatusTypeConstant status, int page, int size, String sortBy, String sortDirection) {
        RoleTypeConstant role = authUtil.roleLogin();
        String statusCode = status == null ? "ALL" : status.name();
        String normalizedSort = (sortBy == null || sortBy.isBlank()) ? "createdAt" : sortBy;
        String normalizedDirection = (sortDirection == null || sortDirection.isBlank())
                ? "DESC"
                : sortDirection.toUpperCase();
        String baseKey = "%s:%s:%d:%d:%s:%s".formatted(
                role.name(),
                statusCode,
                page,
                size,
                normalizedSort,
                normalizedDirection);
        if (role == RoleTypeConstant.CUSTOMER) {
            return baseKey + ":" + authUtil.getLoginId();
        }
        return baseKey;
    }
}
