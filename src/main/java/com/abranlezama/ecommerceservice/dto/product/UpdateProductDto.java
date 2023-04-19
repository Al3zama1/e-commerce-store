package com.abranlezama.ecommerceservice.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
public record UpdateProductDto(
        String name,
        String description,
        Float price,
        Integer stockQuantity
) { }
