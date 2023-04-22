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
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

}
