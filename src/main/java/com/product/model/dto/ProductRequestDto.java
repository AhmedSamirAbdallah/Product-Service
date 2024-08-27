package com.product.model.dto;

import com.fasterxml.jackson.databind.ser.impl.MapEntrySerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequestDto(
        @NotBlank(message = "name can't be empty")
        String name,

        @NotBlank(message = "code can't be empty")
        String code,

        String description,

        @NotNull(message = "price can't be empty")
        BigDecimal price
) {
}
