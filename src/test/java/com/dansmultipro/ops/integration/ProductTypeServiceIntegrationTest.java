package com.dansmultipro.ops.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dansmultipro.ops.dto.master.ProductTypeResponseDto;
import com.dansmultipro.ops.exception.ResourceNotFoundException;
import com.dansmultipro.ops.service.ProductTypeService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductTypeServiceIntegrationTest extends AbstractServiceIntegrationTest {

    @Autowired
    private ProductTypeService productTypeService;

    @Test
    void findAllShouldIncludeDefaultProductType() {
        List<ProductTypeResponseDto> responses = productTypeService.findAll();

        assertThat(responses)
                .extracting(ProductTypeResponseDto::code)
                .contains(defaultProductType.getCode());
    }

    @Test
    void findByIdShouldReturnProductType() {
        ProductTypeResponseDto response = productTypeService.findById(defaultProductType.getId().toString());

        assertThat(response.code()).isEqualTo(defaultProductType.getCode());
        assertThat(response.name()).isEqualTo(defaultProductType.getName());
    }

    @Test
    void findByIdShouldThrowWhenNotFound() {
        assertThatThrownBy(() -> productTypeService.findById(UUID.randomUUID().toString()))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

