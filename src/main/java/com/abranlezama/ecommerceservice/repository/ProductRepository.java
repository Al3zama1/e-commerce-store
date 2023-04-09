package com.abranlezama.ecommerceservice.repository;

import com.abranlezama.ecommerceservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
