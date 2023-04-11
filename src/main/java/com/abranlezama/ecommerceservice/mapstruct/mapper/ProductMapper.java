package com.abranlezama.ecommerceservice.mapstruct.mapper;

import com.abranlezama.ecommerceservice.dto.product.ProductDto;
import com.abranlezama.ecommerceservice.model.Product;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper {

    ProductDto mapProductToDto(Product product);
}
