package com.abranlezama.ecommerceservice.dto.checkout;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record StripeResponseDto(
        @NotBlank String paymentUrl
) { }
