package com.abranlezama.ecommerceservice.service;

import com.abranlezama.ecommerceservice.dto.cart.AddItemToCartDto;
import com.abranlezama.ecommerceservice.dto.cart.CartDto;

public interface CartService {

    CartDto getCartItems(long userId);

    void addItemToShoppingCart(long userId, AddItemToCartDto addItemToCartDto);
}
