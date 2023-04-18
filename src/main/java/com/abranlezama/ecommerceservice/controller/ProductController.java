package com.abranlezama.ecommerceservice.controller;

import com.abranlezama.ecommerceservice.dto.product.AddProductDto;
import com.abranlezama.ecommerceservice.dto.product.ProductDto;
import com.abranlezama.ecommerceservice.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
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
        return productService.getBooks(page, size);
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeProduct(@Positive @PathVariable Long productId) {
        productService.removeProduct(productId);
    }
}
