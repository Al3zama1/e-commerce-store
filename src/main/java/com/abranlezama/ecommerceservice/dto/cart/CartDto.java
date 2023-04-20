package com.abranlezama.ecommerceservice.dto.cart;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.util.List;

@Builder
public record CartDto(
        @Positive Long id,
        @PositiveOrZero Float totalCost,
        List<CartItemDto> cartItems
) { }
