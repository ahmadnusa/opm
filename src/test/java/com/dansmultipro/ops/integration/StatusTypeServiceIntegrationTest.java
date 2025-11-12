package com.dansmultipro.ops.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dansmultipro.ops.constant.StatusTypeConstant;
import com.dansmultipro.ops.dto.master.StatusTypeResponseDto;
import com.dansmultipro.ops.exception.ResourceNotFoundException;
import com.dansmultipro.ops.service.StatusTypeService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class StatusTypeServiceIntegrationTest extends AbstractServiceIntegrationTest {

    @Autowired
    private StatusTypeService statusTypeService;

    @Test
    void findAllShouldReturnAllStatuses() {
        List<StatusTypeResponseDto> responses = statusTypeService.findAll();

        assertThat(responses)
                .extracting(StatusTypeResponseDto::code)
                .contains(StatusTypeConstant.PROCESSING.name(),
                        StatusTypeConstant.APPROVED.name(),
                        StatusTypeConstant.REJECTED.name(),
                        StatusTypeConstant.CANCELLED.name());
    }

    @Test
    void findByIdShouldReturnStatusType() {
        StatusTypeResponseDto response = statusTypeService.findById(processingStatus.getId().toString());

        assertThat(response.code()).isEqualTo(processingStatus.getCode());
        assertThat(response.name()).isEqualTo(processingStatus.getName());
    }

    @Test
    void findByIdShouldThrowWhenNotFound() {
        assertThatThrownBy(() -> statusTypeService.findById(UUID.randomUUID().toString()))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

