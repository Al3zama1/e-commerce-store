package com.abranlezama.ecommerceservice.service.imp;

import com.abranlezama.ecommerceservice.dto.product.AddProductDto;
import com.abranlezama.ecommerceservice.dto.product.ProductDto;
import com.abranlezama.ecommerceservice.dto.product.ProductDtoEmployeeView;
import com.abranlezama.ecommerceservice.dto.product.UpdateProductDto;
import com.abranlezama.ecommerceservice.exception.ExceptionMessages;
import com.abranlezama.ecommerceservice.exception.ProductNotFoundException;
import com.abranlezama.ecommerceservice.mapstruct.mapper.ProductMapper;
import com.abranlezama.ecommerceservice.model.Product;
import com.abranlezama.ecommerceservice.repository.ProductRepository;
import com.abranlezama.ecommerceservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductDto> getProducts(int page, int pageSize) {
        return productRepository
                .findAllByOrderByStockQuantity(PageRequest.of(page, pageSize))
                .stream()
                .map(productMapper::mapProductToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto getProduct(long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);

        if (productOptional.isEmpty()) throw new
                ProductNotFoundException(ExceptionMessages.PRODUCT_NOT_FOUND);

        return productMapper.mapProductToDto(productOptional.get());
    }

    @Override
    public long createProduct(AddProductDto addProductDto) {
        Product product = productMapper.mapProductDtoToEntity(addProductDto);
        product = productRepository.save(product);

        return product.getId();
    }

    @Override
    public void removeProduct(long id) {
        boolean productExists = productRepository.existsById(id);

        if (!productExists) throw new
                ProductNotFoundException(ExceptionMessages.PRODUCT_NOT_FOUND);

        productRepository.deleteById(id);
    }

    @Override
    public void updateProduct(long productId, UpdateProductDto updateProductDto) {
        Optional<Product> productOptional = productRepository.findById(productId);

        if (productOptional.isEmpty()) throw new
                ProductNotFoundException(ExceptionMessages.PRODUCT_NOT_FOUND);

        Product product = productOptional.get();

        product.setName(updateProductDto.name());
        if (updateProductDto.description() != null) product.setDescription(updateProductDto.description());
        product.setPrice(updateProductDto.price());
        product.setStockQuantity(updateProductDto.stockQuantity());

        productRepository.save(product);
    }

    @Override
    public ProductDtoEmployeeView getProductToUpdate(Long productId) {
        return null;
    }
}
