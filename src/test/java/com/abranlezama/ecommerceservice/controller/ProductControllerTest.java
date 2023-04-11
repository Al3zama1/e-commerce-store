package com.abranlezama.ecommerceservice.controller;

import com.abranlezama.ecommerceservice.config.security.CustomAuthenticationEntryPoint;
import com.abranlezama.ecommerceservice.config.security.JwtService;
import com.abranlezama.ecommerceservice.config.security.SecurityConfig;
import com.abranlezama.ecommerceservice.service.AuthenticationService;
import com.abranlezama.ecommerceservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ProductController.class})
@Import(value = {SecurityConfig.class})
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private AuthenticationProvider authenticationProvider;
    @MockBean
    private ProductService productService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;


    @Test
    void shouldCallProductServiceToRetrieveProducts() throws Exception {
        // Given

        // When
        mockMvc.perform(get("/api/v1/products")
                        .param("page", "1")
                        .param("per_page", "2"))
                .andExpect(status().isOk());

        // Then
        then(productService).should().getBooks(1, 2);

    }

    @Test
    void shouldThrow422WhenFetchingProductsWithInvalidRequestParameters() throws Exception {
        // Given

        // When
        mockMvc.perform(get("/api/v1/products")
                        .param("page", "-1")
                        .param("per_page", "-2"))
                .andExpect(status().isUnprocessableEntity());

        // Then
        then(productService).shouldHaveNoInteractions();
    }


}
