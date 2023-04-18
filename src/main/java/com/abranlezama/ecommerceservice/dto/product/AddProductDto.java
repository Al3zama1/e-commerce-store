package com.abranlezama.ecommerceservice.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
public record AddProductDto(
        @NotBlank String name,
        String description,
        @PositiveOrZero Float price,
        @PositiveOrZero Integer stockQuantity
) { }
