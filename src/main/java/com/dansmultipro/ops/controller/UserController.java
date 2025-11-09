package com.dansmultipro.ops.controller;

import com.dansmultipro.ops.dto.common.ApiPutResponseDto;
import com.dansmultipro.ops.dto.user.PasswordUpdateRequestDto;
import com.dansmultipro.ops.dto.user.UserResponseDto;
import com.dansmultipro.ops.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponseDto> getAll(@RequestParam(value = "isActive", required = false) Boolean isActive) {
        return userService.getAll(isActive);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable String id) {
        UserResponseDto response = userService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<ApiPutResponseDto> approve(@PathVariable String id) {
        ApiPutResponseDto response = userService.approveCustomer(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/password")
    public ResponseEntity<ApiPutResponseDto> updatePassword(
            @Valid @RequestBody PasswordUpdateRequestDto request) {
        ApiPutResponseDto response = userService.updatePassword(request);
        return ResponseEntity.ok(response);
    }
}
