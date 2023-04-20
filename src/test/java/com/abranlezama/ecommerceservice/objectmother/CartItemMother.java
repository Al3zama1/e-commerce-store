package com.abranlezama.ecommerceservice.objectmother;

import com.abranlezama.ecommerceservice.model.CartItem;

public class CartItemMother {

    public static CartItem.CartItemBuilder cartItem() {
        return CartItem.builder()
                .quantity((short) 1);
    }
}
