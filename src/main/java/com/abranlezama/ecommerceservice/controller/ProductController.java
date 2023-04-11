package com.abranlezama.ecommerceservice.controller;

import com.abranlezama.ecommerceservice.dto.product.ProductDto;
import com.abranlezama.ecommerceservice.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
