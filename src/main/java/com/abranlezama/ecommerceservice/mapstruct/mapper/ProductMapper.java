package com.abranlezama.ecommerceservice.mapstruct.mapper;

import com.abranlezama.ecommerceservice.dto.product.AddProductDto;
import com.abranlezama.ecommerceservice.dto.product.ProductDto;
import com.abranlezama.ecommerceservice.dto.product.ProductDtoEmployeeView;
import com.abranlezama.ecommerceservice.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ProductMapper {

    ProductDto mapProductToDto(Product product);
    ProductDtoEmployeeView mapProductToEmployeeViewDto(Product product);
    @Mapping(target = "id", ignore = true)
    Product mapProductDtoToEntity(AddProductDto addProductDto);
}
