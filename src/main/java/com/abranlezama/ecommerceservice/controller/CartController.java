package com.abranlezama.ecommerceservice.controller;

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
    public void addItemToCart(@Positive @RequestParam("product") Long productId,
                              @Positive @RequestParam("quantity") Short quantity,
                              @AuthenticationPrincipal User user) {
        cartService.addItemToShoppingCart(user.getId(), productId, quantity);
    }

    @PatchMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCartItem(@Valid @RequestBody CartItemQuantityDto cartItemQuantityDto,
                               @AuthenticationPrincipal User user) {
        cartService.updateCartItem(cartItemQuantityDto.productId(), user.getId(), cartItemQuantityDto.quantity());
    }

    @DeleteMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCartItem(@Positive @RequestParam("product") Long productId,
                               @AuthenticationPrincipal User user) {
        cartService.removeCartItem(productId, user.getId());
    }


}
