package com.abranlezama.ecommerceservice.dto.authentication;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CustomerRegistrationDto(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, max = 20) String password,
        @NotBlank @Size(min = 8, max = 20) String confirmPassword,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String phone,
        @NotNull @Past LocalDate dateOfBirth,
        @NotBlank String street,
        @NotBlank String city,
        @NotBlank String region,
        @NotBlank String postalCode,
        @NotBlank String country

        ) { }
