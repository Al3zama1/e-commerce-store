package com.abranlezama.ecommerceservice.model;

import jakarta.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;
    @Column(nullable = false, length = 20)
    private String name;
    @Column(length = 100)
    private String description;
    @Column(nullable = false)
    private Double price;
    private Integer stock_quantity;
}
