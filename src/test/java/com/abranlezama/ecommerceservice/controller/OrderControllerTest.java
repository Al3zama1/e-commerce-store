package com.abranlezama.ecommerceservice.controller;

import com.abranlezama.ecommerceservice.config.security.CustomAuthenticationEntryPoint;
import com.abranlezama.ecommerceservice.config.security.JwtService;
import com.abranlezama.ecommerceservice.config.security.SecurityConfig;
import com.abranlezama.ecommerceservice.model.Role;
import com.abranlezama.ecommerceservice.model.RoleType;
import com.abranlezama.ecommerceservice.model.User;
import com.abranlezama.ecommerceservice.objectmother.UserMother;
import com.abranlezama.ecommerceservice.service.OrderService;
import com.stripe.model.checkout.Session;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {OrderController.class})
@Import(value = {
        SecurityConfig.class, JwtService.class, CustomAuthenticationEntryPoint.class})
class OrderControllerTest {

    @MockBean
    private OrderService orderService;
    @MockBean
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private MockMvc mockMvc;


    @Test
    void shouldCallOrderServiceToReturnCustomerOrders() throws Exception {
        // Given
        Role role = Role.builder().name(RoleType.CUSTOMER).build();
        User user = UserMother.user()
                .id(1L)
                .roles(Set.of(role))
                .build();

        // When
        mockMvc.perform(get("/api/v1/orders")
                .with(user(user)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void shouldReturn403WhenUnauthorizedToFetchOrders() throws Exception {
        // Given

        // When
        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isForbidden());

        // Then
        then(orderService).shouldHaveNoInteractions();
    }

    @Test
    void shouldCreateCheckoutSession() throws Exception {
        // Given
        Session session = new Session();
        session.setUrl("success");
        Role role = Role.builder().name(RoleType.CUSTOMER).build();
        User user = UserMother.user()
                .id(1L)
                .roles(Set.of(role))
                .build();

        given(orderService.createSession(user)).willReturn(session);

        // When
        mockMvc.perform(post("/api/v1/orders/create-checkout-session")
                .with(user(user)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateCustomerOrder() throws Exception {
        Role role = Role.builder().name(RoleType.CUSTOMER).build();
        User user = UserMother.user()
                .id(1L)
                .roles(Set.of(role))
                .build();

        given(orderService.createOrder(user.getId())).willReturn(1L);

        // When
        mockMvc.perform(post("/api/v1/orders")
                .with(user(user)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", Matchers.containsString("/orders/1")));
    }

    @Test
    @WithMockUser(roles = {"EMPLOYEE"})
    void shouldGive403WhenUnauthorizedUserCreatesOrder() throws Exception {
        // Given

        // When
        mockMvc.perform(post("/api/v1/orders"))
                .andExpect(status().isForbidden());

        // Then
        then(orderService).shouldHaveNoInteractions();

    }

}
