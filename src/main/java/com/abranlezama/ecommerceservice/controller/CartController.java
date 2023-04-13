package com.abranlezama.ecommerceservice.controller;

import com.abranlezama.ecommerceservice.dto.cart.AddItemToCartDto;
import com.abranlezama.ecommerceservice.dto.cart.CartDto;
import com.abranlezama.ecommerceservice.model.User;
import com.abranlezama.ecommerceservice.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public CartDto getCartItems(@AuthenticationPrincipal User user) {
        return cartService.getCartItems(user.getId());
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public void addItemToCart(@Valid @RequestBody AddItemToCartDto addItemToCartDto,
                              @AuthenticationPrincipal User user) {
        cartService.addItemToShoppingCart(user.getId(), addItemToCartDto);
    }


}
