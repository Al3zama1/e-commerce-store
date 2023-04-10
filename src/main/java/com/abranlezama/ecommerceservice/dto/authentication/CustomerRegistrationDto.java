package com.abranlezama.ecommerceservice.dto.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CustomerRegistrationDto(
        @Email String email,
        @NotBlank String password,
        @NotBlank String confirmPassword,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String phone,
        @NotNull LocalDate dateOfBirth,
        @NotBlank String street,
        @NotBlank String city,
        @NotBlank String region,
        @NotBlank String postalCode,
        @NotBlank String country

        ) { }
