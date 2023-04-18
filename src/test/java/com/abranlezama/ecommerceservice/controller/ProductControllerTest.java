package com.abranlezama.ecommerceservice.controller;

import com.abranlezama.ecommerceservice.config.security.CustomAuthenticationEntryPoint;
import com.abranlezama.ecommerceservice.config.security.JwtService;
import com.abranlezama.ecommerceservice.config.security.SecurityConfig;
import com.abranlezama.ecommerceservice.dto.product.AddProductDto;
import com.abranlezama.ecommerceservice.dto.product.UpdateProductDto;
import com.abranlezama.ecommerceservice.objectmother.ProductMother;
import com.abranlezama.ecommerceservice.service.AuthenticationService;
import com.abranlezama.ecommerceservice.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ProductController.class})
@Import(value = {SecurityConfig.class, CustomAuthenticationEntryPoint.class})
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
    @Autowired
    private ObjectMapper mapper;


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

    @Test
    @WithMockUser(roles = {"EMPLOYEE"})
    void shouldCallProductServiceToAddNewProductAndReturnIdOfCreatedProduct() throws Exception {
        // Given
        AddProductDto addProductDto = ProductMother.addProductDto().build();
        given(productService.createProduct(addProductDto)).willReturn(1L);

        // When
        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(addProductDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", Matchers.containsString("/products/1")));

        // Then
        then(productService).should().createProduct(addProductDto);
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldReturn403WhenNotAuthorizedToAddNewProduct() throws Exception {
        // Given
        AddProductDto addProductDto = ProductMother.addProductDto().build();

        // When
        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(addProductDto)))
                .andExpect(status().isForbidden());

        // Then
        then(productService).shouldHaveNoInteractions();
    }

    @Test
    void shouldRedirectUserToAuthenticateWhenCreatingProductUnauthenticated() throws Exception {
        // Given

        // When
        mockMvc.perform(post("/api/v1/products"))
                .andExpect(status().is3xxRedirection());

        // Then
        then(productService).shouldHaveNoInteractions();
    }

    @Test
    @WithMockUser(roles = {"EMPLOYEE"})
    void shouldCallProductServiceToRemoveProduct() throws Exception {
        // Given
        long productId = 1L;

        // When
        mockMvc.perform(delete("/api/v1/products/{id}", productId))
                .andExpect(status().isNoContent());

        // Then
        then(productService).should().removeProduct(productId);
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldReturn403WhenUserDoesNotHavePermissionToRemoveProduct() throws Exception {
        // Given
        long productId = 1L;

        // When
        mockMvc.perform(delete("/api/v1/products/{id}", productId))
                .andExpect(status().isForbidden());

        // Then
        then(productService).shouldHaveNoInteractions();
    }

    @Test
    void shouldRedirectUserToAuthenticateWhenRemovingProductUnauthenticated() throws Exception {
        // Given
        long productId = 1L;

        // When
        mockMvc.perform(delete("/api/v1/products/{id}", productId))
                .andExpect(status().is3xxRedirection());

        // Then
        then(productService).shouldHaveNoInteractions();
    }

    @Test
    @WithMockUser(roles = {"EMPLOYEE"})
    void shouldReturn422WhenIdOfProductToRemoveIsNegative() throws Exception {
        // Given
        long productId = -1;

        // When
        mockMvc.perform(delete("/api/v1/products/{id}", productId))
                .andExpect(status().isUnprocessableEntity());

        // Then
        then(productService).shouldHaveNoInteractions();
    }

    @Test
    @WithMockUser(roles = {"EMPLOYEE"})
    void shouldCallProductServiceLogicToUpdateProduct() throws Exception {
        // Given
        long productId = 1L;
        UpdateProductDto updateProductDto = ProductMother.updateProductDto().build();

        // When
        mockMvc.perform(patch("/api/v1/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateProductDto)))
                .andExpect(status().isNoContent());

        // Then
        then(productService).should().updateProduct(productId, updateProductDto);
    }

    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    void shouldReturn403WhenNotAuthorizedToUpdateProduct() throws Exception {
        // Given
        long productId = 1L;
        UpdateProductDto updateProductDto = ProductMother.updateProductDto().build();

        // When
        mockMvc.perform(patch("/api/v1/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateProductDto)))
                .andExpect(status().isForbidden());

        // Then
        then(productService).shouldHaveNoInteractions();
    }

    @Test
    void shouldRedirectUserToAuthenticateWhenUpdatingProductAndNotAuthenticated() throws Exception {
        // Given
        long productId = 1L;
        UpdateProductDto updateProductDto = ProductMother.updateProductDto().build();

        // When
        mockMvc.perform(patch("/api/v1/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateProductDto)))
                .andExpect(status().is3xxRedirection());

        // Then
        then(productService).shouldHaveNoInteractions();
    }


}
