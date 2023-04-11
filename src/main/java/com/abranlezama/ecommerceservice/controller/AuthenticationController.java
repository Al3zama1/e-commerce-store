package com.abranlezama.ecommerceservice.controller;

import com.abranlezama.ecommerceservice.dto.authentication.AuthRequestDto;
import com.abranlezama.ecommerceservice.dto.authentication.AuthResponseDto;
import com.abranlezama.ecommerceservice.dto.authentication.CustomerRegistrationDto;
import com.abranlezama.ecommerceservice.service.AuthenticationService;
import com.abranlezama.ecommerceservice.service.imp.AuthenticationServiceImp;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping("/login")
    public String login() {
        return "you are in the login page";
    }

    @PostMapping("/login")
    public AuthResponseDto login(@Valid @RequestBody AuthRequestDto authRequestDto) {
        return authenticationService.login(authRequestDto);
    }

    @PostMapping("/customer/register")
    public void registerCustomer(
            @Valid @RequestBody CustomerRegistrationDto registrationRequestDto) {
        authenticationService.registerCustomer(registrationRequestDto);
    }

}
