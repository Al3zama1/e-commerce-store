package com.abranlezama.ecommerceservice.objectmother;

import com.abranlezama.ecommerceservice.dto.product.AddProductDto;
import com.abranlezama.ecommerceservice.model.Product;

public class ProductMother {

    public static AddProductDto.AddProductDtoBuilder addProductDto() {
        return AddProductDto.builder()
                .name("Play Station 4")
                .description("Next generation gaming console.")
                .price(500F)
                .stockQuantity(100);
    }

    public static Product.ProductBuilder saveProduct() {
        return Product.builder()
                .name("Play Station 4")
                .description("Next generation gaming console.")
                .price(500F)
                .stockQuantity(100);

    }
}
