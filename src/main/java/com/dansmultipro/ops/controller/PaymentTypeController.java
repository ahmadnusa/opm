package com.dansmultipro.ops.controller;

import com.dansmultipro.ops.dto.master.PaymentTypeResponseDto;
import com.dansmultipro.ops.service.PaymentTypeService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment-types")
@SecurityRequirement(name = "bearerAuth")
public class PaymentTypeController {

    private final PaymentTypeService service;

    public PaymentTypeController(PaymentTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<PaymentTypeResponseDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentTypeResponseDto> findById(@PathVariable String id) {
        PaymentTypeResponseDto res = service.findById(id);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<PaymentTypeResponseDto> findByCode(@PathVariable String code) {
        PaymentTypeResponseDto res = service.findByCode(code);
        return ResponseEntity.ok(res);
    }
}
