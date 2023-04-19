package com.abranlezama.ecommerceservice.controller;

import com.abranlezama.ecommerceservice.dto.product.AddProductDto;
import com.abranlezama.ecommerceservice.dto.product.ProductDto;
import com.abranlezama.ecommerceservice.dto.product.ProductDtoEmployeeView;
import com.abranlezama.ecommerceservice.dto.product.UpdateProductDto;
import com.abranlezama.ecommerceservice.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Validated
@RequiredArgsConstructor
@Tag(name = "Products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDto> getProducts(@Positive @RequestParam(name = "page", defaultValue = "1") Integer page,
                                        @Positive @RequestParam(name = "per_page",defaultValue = "20") Integer size) {
        return productService.getProducts(page, size);
    }

    @GetMapping("/{productId}")
    public ProductDto getProduct(@Positive @PathVariable Long productId) {
        return productService.getProduct(productId);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<Void> createProduct(@Valid @RequestBody AddProductDto addProductDto,
                                              UriComponentsBuilder uriComponentsBuilder) {
        long productId = productService.createProduct(addProductDto);

        UriComponents uriComponents = uriComponentsBuilder.path("/api/v1/products/{id}").buildAndExpand(productId);
        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeProduct(@Positive @PathVariable Long productId) {
        productService.removeProduct(productId);
    }

    @GetMapping("/update/{productId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public ProductDtoEmployeeView getProductToUpdate(@Positive @PathVariable Long productId) {
        return productService.getProductToUpdate(productId);
    }

    @PatchMapping("/{productId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProduct(@Valid @RequestBody UpdateProductDto updateProductDto,
                              @Positive @PathVariable Long productId) {
        productService.updateProduct(productId, updateProductDto);
    }
}
