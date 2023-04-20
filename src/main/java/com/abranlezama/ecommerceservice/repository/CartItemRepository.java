package com.abranlezama.ecommerceservice.repository;

import com.abranlezama.ecommerceservice.model.CartItem;
import com.abranlezama.ecommerceservice.model.CartItemPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, CartItemPK> {
}
