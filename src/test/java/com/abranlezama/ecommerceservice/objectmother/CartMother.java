package com.abranlezama.ecommerceservice.objectmother;

import com.abranlezama.ecommerceservice.model.Cart;
import com.abranlezama.ecommerceservice.model.CartItem;

public class CartMother {

    public static Cart.CartBuilder cart() {
        return Cart.builder()
                .id(1L)
                .totalCost(100F);
    }
}
