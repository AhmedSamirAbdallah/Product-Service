package com.product.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequestDto(
        @NotBlank(message = "name can't be empty") String name,
        @NotBlank(message = "description can't be empty") String description,
        @NotNull(message = "price can't be empty") BigDecimal price
) {
}
