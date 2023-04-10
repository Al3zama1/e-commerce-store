package com.abranlezama.ecommerceservice.dto.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthRequestDto(
        @Email String email,
        @NotBlank String password
) {
}
