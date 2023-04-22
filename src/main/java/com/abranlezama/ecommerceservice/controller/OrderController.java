package com.abranlezama.ecommerceservice.controller;

import com.abranlezama.ecommerceservice.dto.checkout.StripeResponseDto;
import com.abranlezama.ecommerceservice.model.User;
import com.abranlezama.ecommerceservice.service.OrderService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create-checkout-session")
    @PreAuthorize("hasRole('CUSTOMER')")
    public StripeResponseDto checkoutList(@AuthenticationPrincipal User user) {
        Session session = orderService.createSession(user);
        return new StripeResponseDto(session.getUrl());
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Void> createOrder(@AuthenticationPrincipal User user,
                                            UriComponentsBuilder uriComponentsBuilder) {
        long orderId = orderService.createOrder(user.getId());
        UriComponents uriComponents = uriComponentsBuilder.path("/api/v1/orders/{id}").buildAndExpand(orderId);

        return ResponseEntity.created(uriComponents.toUri()).build();
    }
}
