package com.abranlezama.ecommerceservice.service.imp;

import com.abranlezama.ecommerceservice.dto.cart.AddItemToCartDto;
import com.abranlezama.ecommerceservice.dto.cart.CartDto;
import com.abranlezama.ecommerceservice.service.CartService;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImp implements CartService {
    @Override
    public CartDto getCartItems(long userId) {
        return null;
    }

    @Override
    public void addItemToShoppingCart(long userId, AddItemToCartDto addItemToCartDto) {

    }

    @Override
    public void updateCartItem(long productId, long userId, short quantity) {

    }
}
