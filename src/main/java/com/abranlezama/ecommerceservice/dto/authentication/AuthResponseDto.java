package com.abranlezama.ecommerceservice.dto.authentication;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthResponseDto(
        @NotBlank String token
) {
}
