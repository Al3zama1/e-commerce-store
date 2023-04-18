package com.abranlezama.ecommerceservice.service;

import com.abranlezama.ecommerceservice.dto.product.AddProductDto;
import com.abranlezama.ecommerceservice.dto.product.ProductDto;

import java.util.List;

public interface ProductService {

    List<ProductDto> getBooks(int page, int pageSize);
    long createProduct(AddProductDto addProductDto);
}
