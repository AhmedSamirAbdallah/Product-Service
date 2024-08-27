package com.product.model.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(value = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    private String id;

    @NotBlank(message = "Name must not be blank")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    @Indexed(unique = true)
    private String name;

    @NotBlank(message = "Code must not be blank")
    @Size(min = 1, max = 100, message = "Code must be between 1 and 100 characters")
    @Indexed(unique = true)
    private String code;

    private String description;

    @NotNull(message = "Price must not be null")
    private BigDecimal price;

}
