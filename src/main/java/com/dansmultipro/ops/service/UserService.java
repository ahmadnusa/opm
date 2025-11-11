package com.dansmultipro.ops.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.dansmultipro.ops.dto.auth.RegisterRequestDto;
import com.dansmultipro.ops.dto.common.ApiPostResponseDto;
import com.dansmultipro.ops.dto.common.ApiPutResponseDto;
import com.dansmultipro.ops.dto.user.PasswordUpdateRequestDto;
import com.dansmultipro.ops.dto.user.UserResponseDto;

public interface UserService extends UserDetailsService {

    ApiPostResponseDto register(RegisterRequestDto request);

    ApiPutResponseDto updatePassword(PasswordUpdateRequestDto request);

    ApiPutResponseDto approveCustomer(List<String> customerIds);

    List<UserResponseDto> getAll(Boolean isActive, String roleCode);

    UserResponseDto getById(String id);
}
