package com.abranlezama.ecommerceservice.controller;

import com.abranlezama.ecommerceservice.config.security.CustomAuthenticationEntryPoint;
import com.abranlezama.ecommerceservice.config.security.JwtService;
import com.abranlezama.ecommerceservice.config.security.SecurityConfig;
import com.abranlezama.ecommerceservice.dto.cart.AddItemToCartDto;
import com.abranlezama.ecommerceservice.dto.cart.CartItemQuantityDto;
import com.abranlezama.ecommerceservice.model.Role;
import com.abranlezama.ecommerceservice.model.RoleType;
import com.abranlezama.ecommerceservice.model.User;
import com.abranlezama.ecommerceservice.objectmother.UserMother;
import com.abranlezama.ecommerceservice.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {CartController.class})
@Import(value = {
        SecurityConfig.class, JwtService.class, CustomAuthenticationEntryPoint.class})
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationProvider authenticationProvider;
    @MockBean
    private CartService cartService;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void shouldCallBusinessLogicToReturnCustomerCartItems() throws Exception {
        // Given
        Role role = Role.builder().name(RoleType.CUSTOMER).build();
       User user = UserMother.user()
               .id(1L)
               .roles(Set.of(role))
               .build();

        // When
        mockMvc.perform(get("/api/v1/cart")
                .with(user(user)))
                .andExpect(status().isOk());

        // Then
        then(cartService).should().getCartItems(user.getId());
    }


    @Test
    void shouldAddProductToShoppingCart() throws Exception {
        // Given
        Role role = Role.builder().name(RoleType.CUSTOMER).build();
        User user = UserMother.user()
                .id(1L)
                .roles(Set.of(role))
                .build();
        AddItemToCartDto addItemToCartDto = new AddItemToCartDto(1L, (short) 3);

        // When
        mockMvc.perform(post("/api/v1/cart")
                .with(user(user))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addItemToCartDto)))
                .andExpect(status().isOk());

        // Then
        then(cartService).should().addItemToShoppingCart(user.getId(), addItemToCartDto);
    }

    @Test
    void shouldCallCartServiceToUpdateCartItem() throws Exception {
        // Given
        long productId = 1L;
        Role role = Role.builder().name(RoleType.CUSTOMER).build();
        User user = UserMother.user()
                .id(1L)
                .roles(Set.of(role))
                .build();
        CartItemQuantityDto itemQuantity = new CartItemQuantityDto((short) 3);

        // When
        mockMvc.perform(patch("/api/v1/cart/{productId}", productId)
                .with(user(user))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemQuantity)))
                .andExpect(status().isNoContent());

        // Then
        then(cartService).should().updateCartItem(productId, user.getId(), itemQuantity.quantity());
    }


}
