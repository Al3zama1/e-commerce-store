package com.abranlezama.ecommerceservice.mapstruct.mapper;

import com.abranlezama.ecommerceservice.dto.product.ProductDto;
import com.abranlezama.ecommerceservice.model.Product;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {
    private static final ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Test
    void shouldCorrectlyConvertProductToProductDto() {
        // Given
        Product product = Product.builder()
                .id(1L)
                .name("Game of Thrones")
                .description("This is a great book about medieval times.")
                .price(10F)
                .stockQuantity(20)
                .build();

        // When
        ProductDto productDto = INSTANCE.mapProductToDto(product);

        // Then
        assertThat(productDto.id()).isEqualTo(product.getId());
        assertThat(productDto.name()).isEqualTo(product.getName());
        assertThat(productDto.description()).isEqualTo(product.getDescription());
        assertThat(productDto.price()).isEqualTo(product.getPrice());
    }

}
