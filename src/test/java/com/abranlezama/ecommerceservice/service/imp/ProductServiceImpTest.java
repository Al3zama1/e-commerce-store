package com.abranlezama.ecommerceservice.service.imp;

import com.abranlezama.ecommerceservice.dto.product.AddProductDto;
import com.abranlezama.ecommerceservice.mapstruct.mapper.ProductMapper;
import com.abranlezama.ecommerceservice.model.Product;
import com.abranlezama.ecommerceservice.objectmother.ProductMother;
import com.abranlezama.ecommerceservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(SpringExtension.class)
class ProductServiceImpTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;
    @InjectMocks
    private ProductServiceImp cut;

    @Test
    void shouldReturnProductsForGivenPage() {
        // Given
        int page = 1;
        int size = 20;
        Pageable pageable = PageRequest.of(page, size);

        given(productRepository.findAllByOrderByStockQuantity(pageable))
                .willReturn(List.of());

        // When
        cut.getBooks(page, size);

        // Then
        then(productRepository).should().findAllByOrderByStockQuantity(pageable);
    }

    @Test
    void shouldCreateNewProduct() {
        // Given
        AddProductDto addProductDto = ProductMother.addProductDto().build();
        Product product = ProductMother.saveProduct().build();

        given(productMapper.mapProductDtoToEntity(addProductDto)).willReturn(product);
        given(productRepository.save(product)).willAnswer(invocation -> {
            Product savedProduct = invocation.getArgument(0);
            savedProduct.setId(1L);
            return savedProduct;
        });

        // When
        cut.createProduct(addProductDto);

        // Then
        then(productRepository).should().save(product);
    }

}
