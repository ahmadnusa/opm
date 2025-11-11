package com.dansmultipro.ops.controller;

import com.dansmultipro.ops.dto.auth.RegisterRequestDto;
import com.dansmultipro.ops.dto.common.ApiPostResponseDto;
import com.dansmultipro.ops.dto.common.ApiPutResponseDto;
import com.dansmultipro.ops.dto.user.PasswordUpdateRequestDto;
import com.dansmultipro.ops.dto.user.UserApproveRequestDto;
import com.dansmultipro.ops.dto.user.UserResponseDto;
import com.dansmultipro.ops.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiPostResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
        ApiPostResponseDto response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('SA')")
    @GetMapping
    public ResponseEntity<List<UserResponseDto> >getAll(@RequestParam(required = false) Boolean isActive,
                                        @RequestParam(required = false) String roleCode) {
        List<UserResponseDto> res = userService.getAll(isActive, roleCode);
        return ResponseEntity.ok(res);
    }

    @PreAuthorize("hasRole('SA')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable String id) {
        UserResponseDto response = userService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('SA')")
    @PutMapping("/approve")
    public ResponseEntity<ApiPutResponseDto> approve(
            @RequestParam UserApproveRequestDto request
            ) {
        ApiPutResponseDto response = userService.approveCustomer(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/password")
    public ResponseEntity<ApiPutResponseDto> updatePassword(
            @Valid @RequestBody PasswordUpdateRequestDto request) {
        ApiPutResponseDto response = userService.updatePassword(request);
        return ResponseEntity.ok(response);
    }
}
