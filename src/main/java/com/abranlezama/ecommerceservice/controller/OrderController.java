package com.abranlezama.ecommerceservice.controller;

import com.abranlezama.ecommerceservice.dto.checkout.StripeResponseDto;
import com.abranlezama.ecommerceservice.dto.order.OrderDto;
import com.abranlezama.ecommerceservice.dto.order.OrderItemDto;
import com.abranlezama.ecommerceservice.model.OrderItem;
import com.abranlezama.ecommerceservice.model.User;
import com.abranlezama.ecommerceservice.service.OrderService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Orders")
@Validated
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<OrderDto> getOrders(@AuthenticationPrincipal User user) {
        return orderService.getOrders(user.getId());
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<OrderItemDto> getOrder(@AuthenticationPrincipal User user,
                                       @Positive @PathVariable Long orderId) {
        return orderService.getOrder(orderId, user.getId());
    }

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
