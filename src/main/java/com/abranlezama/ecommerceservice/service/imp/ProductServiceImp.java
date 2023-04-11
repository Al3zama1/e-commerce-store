package com.abranlezama.ecommerceservice.service.imp;

import com.abranlezama.ecommerceservice.dto.product.ProductDto;
import com.abranlezama.ecommerceservice.mapstruct.mapper.ProductMapper;
import com.abranlezama.ecommerceservice.repository.ProductRepository;
import com.abranlezama.ecommerceservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductDto> getBooks(int page, int pageSize) {
        return productRepository
                .findAllByOrderByStockQuantity(PageRequest.of(page, pageSize))
                .stream()
                .map(productMapper::mapProductToDto)
                .collect(Collectors.toList());
    }
}
