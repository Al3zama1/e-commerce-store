package com.abranlezama.ecommerceservice.objectmother;

import com.abranlezama.ecommerceservice.model.OrderItem;

public class OrderItemMother {

    public static OrderItem.OrderItemBuilder orderItem() {
        return OrderItem.builder()
                .id(1L)
                .quantity((short) 2);
    }
}
