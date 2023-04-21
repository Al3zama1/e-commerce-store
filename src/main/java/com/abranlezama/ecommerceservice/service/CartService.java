package com.abranlezama.ecommerceservice.service;

import com.abranlezama.ecommerceservice.dto.cart.CartDto;

public interface CartService {

    CartDto getCartItems(long userId);

    void addItemToShoppingCart(long userId, long productId, short quantity);

    void updateCartItem(long productId, long userId, short quantity);

    void removeCartItem(long productId, long userId);
}
