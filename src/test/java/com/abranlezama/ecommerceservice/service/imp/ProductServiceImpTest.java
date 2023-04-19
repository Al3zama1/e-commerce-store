package com.abranlezama.ecommerceservice.service.imp;

import com.abranlezama.ecommerceservice.dto.product.AddProductDto;
import com.abranlezama.ecommerceservice.dto.product.UpdateProductDto;
import com.abranlezama.ecommerceservice.exception.ExceptionMessages;
import com.abranlezama.ecommerceservice.exception.ProductNotFoundException;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

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
        cut.getProducts(page, size);

        // Then
        then(productRepository).should().findAllByOrderByStockQuantity(pageable);
    }

    @Test
    void shouldReturnSingleProduct() {
        // Given
        long productId = 1L;
        given(productRepository.findById(productId)).willReturn(Optional.of(new Product()));

        // When
        cut.getProduct(productId);

        // Then
        then(productRepository).should().findById(productId);
    }

    @Test
    void shouldThrowProductNotFoundExceptionWhenProductWithGivenIdDoesNotExist() {
        //  Given
        long productId = 1L;
        given(productRepository.findById(productId)).willReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> cut.getProduct(productId))
                .hasMessage(ExceptionMessages.PRODUCT_NOT_FOUND)
                .isInstanceOf(ProductNotFoundException.class);

        // Then
        then(productRepository).should().findById(productId);
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

    @Test
    void shouldRemoveProduct() {
        // Given
        long productId = 1L;

        given(productRepository.existsById(productId)).willReturn(true);

        // When
        cut.removeProduct(productId);

        // Then
        then(productRepository).should().deleteById(productId);
    }

    @Test
    void shouldThrowProductNotFoundExceptionWhenDeletingNonExistingProduct() {
        // Given
        long productId = 1L;

        given(productRepository.existsById(productId)).willReturn(false);

        // When
        assertThatThrownBy(() -> cut.removeProduct(productId))
                .withFailMessage(ExceptionMessages.PRODUCT_NOT_FOUND)
                .isInstanceOf(ProductNotFoundException.class);

        // Then
        then(productRepository).should(never()).deleteById(any());
    }

    @Test
    void shouldUpdateExistingProduct() {
        // Given
        long productId = 1L;
        UpdateProductDto updateProductDto = ProductMother.updateProductDto().build();
        Product product = ProductMother.saveProduct()
                        .id(productId)
                        .build();

        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        // When
        cut.updateProduct(productId, updateProductDto);

        // Then
        then(productRepository).should().save(product);
    }

    @Test
    void shouldThrowProductNotFoundExceptionWhenUpdatingNonExistingProduct() {
        // Given
        long productId = 1L;
        UpdateProductDto updateProductDto = ProductMother.updateProductDto().build();
        Product product = ProductMother.saveProduct()
                .id(productId)
                .build();

        given(productRepository.findById(productId)).willReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> cut.updateProduct(productId, updateProductDto))
                .hasMessage(ExceptionMessages.PRODUCT_NOT_FOUND)
                .isInstanceOf(ProductNotFoundException.class);

        // Then
        then(productRepository).should(never()).save(any());
    }

    @Test
    void shouldReturnProductToUpdate() {
        // Given
        long productId = 1L;
        Product product = ProductMother.saveProduct()
                .id(productId)
                .build();


        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        // When
        cut.getProductToUpdate(productId);

        // Then
        then(productRepository).should().findById(productId);
    }

    @Test
    void shouldThrowProductNotFoundExceptionWhenRequestingProductToUpdateThatDoesNotExist() {
        // Given
        long productId = 1L;

        given(productRepository.findById(productId)).willReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> cut.getProductToUpdate(productId))
                .hasMessage(ExceptionMessages.PRODUCT_NOT_FOUND)
                .isInstanceOf(ProductNotFoundException.class);

        // Then
        then(productMapper).shouldHaveNoInteractions();
    }

}
