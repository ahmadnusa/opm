package com.dansmultipro.ops.service;

import com.dansmultipro.ops.dto.master.StatusTypeResponseDto;

import java.util.List;

public interface StatusTypeService {

    List<StatusTypeResponseDto> findAll();

    StatusTypeResponseDto findById(String id);

    StatusTypeResponseDto findByCode(String code);
}
