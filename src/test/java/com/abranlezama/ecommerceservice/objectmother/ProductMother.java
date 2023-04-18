package com.abranlezama.ecommerceservice.objectmother;

import com.abranlezama.ecommerceservice.dto.product.AddProductDto;

public class ProductMother {

    public static AddProductDto.AddProductDtoBuilder addProductDto() {
        return AddProductDto.builder()
                .name("Play Station 4")
                .description("Next generation gaming console.")
                .price(500F)
                .stockQuantity(100);
    }
}
