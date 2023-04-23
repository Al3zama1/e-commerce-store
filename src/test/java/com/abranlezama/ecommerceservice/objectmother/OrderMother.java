package com.abranlezama.ecommerceservice.objectmother;

import com.abranlezama.ecommerceservice.model.CustomerOrder;

import java.time.LocalDateTime;

public class OrderMother {

    public static CustomerOrder.CustomerOrderBuilder order() {
        return CustomerOrder.builder()
                .id(1L)
                .datePlaced(LocalDateTime.now());
    }
}
