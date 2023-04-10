package com.abranlezama.ecommerceservice.controller;

import com.abranlezama.ecommerceservice.config.security.CustomAuthenticationEntryPoint;
import com.abranlezama.ecommerceservice.config.security.JwtService;
import com.abranlezama.ecommerceservice.config.security.SecurityConfig;
import com.abranlezama.ecommerceservice.dto.authentication.CustomerRegistrationDto;
import com.abranlezama.ecommerceservice.objectmother.CustomerRegistrationDtoMother;
import com.abranlezama.ecommerceservice.repository.UserRepository;
import com.abranlezama.ecommerceservice.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@Import(value = {JwtService.class, SecurityConfig.class, CustomAuthenticationEntryPoint.class})
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private AuthenticationProvider authenticationProvider;


    @Test
    void shouldCallBusinessLogicToRegisterCustomer() throws Exception {
        // Given
        CustomerRegistrationDto registrationDto = CustomerRegistrationDtoMother
                .registrationDto().build();

        // When
        mockMvc.perform(post("/api/v1/customer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isOk());

        // Then
        then(authenticationService).should().registerCustomer(registrationDto);
    }

    @Test
    void shouldThrow400WhenInvalidCustomerRegistrationInput() throws Exception {
        // Given
        CustomerRegistrationDto registrationDto = CustomerRegistrationDtoMother
                .registrationDto()
                .password("43535")
                .build();

        // When
        mockMvc.perform(post("/api/v1/customer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isBadRequest());

        // Then
        then(authenticationService).shouldHaveNoInteractions();
    }


}
