package com.abranlezama.ecommerceservice.dto.cart;

import com.abranlezama.ecommerceservice.dto.product.ProductDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CartItem(
        @NotBlank ProductDto product,
        @Positive Short quantity
) {
}
