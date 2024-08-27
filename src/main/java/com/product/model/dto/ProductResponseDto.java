package com.product.model.dto;

import java.math.BigDecimal;

public record ProductResponseDto(
        String id,
        String name,
        String code,
        String description,
        BigDecimal price
) {
}
