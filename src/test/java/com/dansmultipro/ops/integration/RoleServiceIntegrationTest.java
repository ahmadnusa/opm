package com.dansmultipro.ops.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dansmultipro.ops.dto.master.RoleResponseDto;
import com.dansmultipro.ops.exception.ResourceNotFoundException;
import com.dansmultipro.ops.service.RoleService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RoleServiceIntegrationTest extends AbstractServiceIntegrationTest {

    @Autowired
    private RoleService roleService;

    @Test
    void findAllShouldReturnAllRoles() {
        List<RoleResponseDto> responses = roleService.findAll();

        assertThat(responses)
                .extracting(RoleResponseDto::code)
                .contains(superAdminRole.getCode(), customerRole.getCode(), gatewayRole.getCode(), systemRole.getCode());
    }

    @Test
    void findByIdShouldReturnRole() {
        RoleResponseDto response = roleService.findById(superAdminRole.getId().toString());

        assertThat(response.code()).isEqualTo(superAdminRole.getCode());
        assertThat(response.name()).isEqualTo(superAdminRole.getName());
    }

    @Test
    void findByIdShouldThrowWhenNotFound() {
        assertThatThrownBy(() -> roleService.findById(UUID.randomUUID().toString()))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

