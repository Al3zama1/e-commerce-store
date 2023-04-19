package com.abranlezama.ecommerceservice.service;

import com.abranlezama.ecommerceservice.dto.product.AddProductDto;
import com.abranlezama.ecommerceservice.dto.product.ProductDto;
import com.abranlezama.ecommerceservice.dto.product.ProductDtoEmployeeView;
import com.abranlezama.ecommerceservice.dto.product.UpdateProductDto;

import java.util.List;

public interface ProductService {

    List<ProductDto> getProducts(int page, int pageSize);
    ProductDto getProduct(long productId);
    long createProduct(AddProductDto addProductDto);
    void removeProduct(long id);
    void updateProduct(long productId, UpdateProductDto updateProductDto);
    ProductDtoEmployeeView getProductToUpdate(Long productId);
}
