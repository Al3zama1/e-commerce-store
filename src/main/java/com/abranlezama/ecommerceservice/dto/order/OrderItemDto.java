package com.abranlezama.ecommerceservice.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record OrderItemDto(
        @Positive Long productId,
        @NotBlank String productName,
        @Positive Short quantity,
        @Positive Float price
) { }
