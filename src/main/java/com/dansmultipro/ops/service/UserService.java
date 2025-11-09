package com.dansmultipro.ops.service;

import com.dansmultipro.ops.dto.auth.LoginResponseDto;
import com.dansmultipro.ops.dto.auth.RegisterRequestDto;
import com.dansmultipro.ops.dto.common.ApiPostResponseDto;
import com.dansmultipro.ops.dto.common.ApiPutResponseDto;
import com.dansmultipro.ops.dto.user.PasswordUpdateRequestDto;
import com.dansmultipro.ops.dto.user.UserResponseDto;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    ApiPostResponseDto register(RegisterRequestDto request);

    LoginResponseDto login(String email);

    ApiPutResponseDto updatePassword(PasswordUpdateRequestDto request);

    ApiPutResponseDto approveCustomer(String id);

    List<UserResponseDto> getAll();

    UserResponseDto getById(String id);
}
