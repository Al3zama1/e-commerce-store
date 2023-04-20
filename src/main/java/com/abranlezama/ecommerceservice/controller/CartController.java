package com.abranlezama.ecommerceservice.controller;

import com.abranlezama.ecommerceservice.dto.cart.AddItemToCartDto;
import com.abranlezama.ecommerceservice.dto.cart.CartDto;
import com.abranlezama.ecommerceservice.dto.cart.CartItemQuantityDto;
import com.abranlezama.ecommerceservice.model.User;
import com.abranlezama.ecommerceservice.service.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@Tag(name = "Cart")
@RequiredArgsConstructor
@Validated
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

    @PatchMapping("/{productId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCartItem(@Valid @RequestBody CartItemQuantityDto cartItemQuantityDto,
                               @Positive @PathVariable Long productId,
                               @AuthenticationPrincipal User user) {
        cartService.updateCartItem(productId, user.getId(), cartItemQuantityDto.quantity());
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCartItem(@Positive @PathVariable Long productId,
                               @AuthenticationPrincipal User user) {
        cartService.removeCartItem(productId, user.getId());
    }


}
