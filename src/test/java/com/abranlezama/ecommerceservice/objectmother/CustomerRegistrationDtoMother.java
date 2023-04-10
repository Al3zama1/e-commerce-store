package com.abranlezama.ecommerceservice.objectmother;

import com.abranlezama.ecommerceservice.dto.authentication.CustomerRegistrationDto;

import java.time.LocalDate;

public class CustomerRegistrationDtoMother {

    public static CustomerRegistrationDto.CustomerRegistrationDtoBuilder registrationDto() {
        return CustomerRegistrationDto.builder()
                .email("john.last@gmail.com")
                .password("%W0rldFine001")
                .confirmPassword("%W0rldFine001")
                .firstName("John")
                .lastName("Last")
                .dateOfBirth(LocalDate.of(2000, 10, 3))
                .phone("2137665566")
                .street("1233 S 55Th St")
                .city("Los Angeles")
                .region("CA")
                .postalCode("90002")
                .country("US");
    }
}
