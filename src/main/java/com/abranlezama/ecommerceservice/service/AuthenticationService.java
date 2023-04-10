package com.abranlezama.ecommerceservice.service;

import com.abranlezama.ecommerceservice.dto.authentication.AuthRequestDto;
import com.abranlezama.ecommerceservice.dto.authentication.AuthResponseDto;
import com.abranlezama.ecommerceservice.dto.authentication.CustomerRegistrationDto;

public interface AuthenticationService {
    AuthResponseDto login(AuthRequestDto authRequestDto);
    void registerCustomer(CustomerRegistrationDto registrationDto);
}
