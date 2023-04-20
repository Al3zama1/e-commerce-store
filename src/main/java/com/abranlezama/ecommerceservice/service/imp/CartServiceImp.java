package com.abranlezama.ecommerceservice.service.imp;

import com.abranlezama.ecommerceservice.dto.cart.AddItemToCartDto;
import com.abranlezama.ecommerceservice.dto.cart.CartDto;
import com.abranlezama.ecommerceservice.mapstruct.mapper.CartMapper;
import com.abranlezama.ecommerceservice.model.Cart;
import com.abranlezama.ecommerceservice.model.Customer;
import com.abranlezama.ecommerceservice.repository.CartRepository;
import com.abranlezama.ecommerceservice.repository.CustomerRepository;
import com.abranlezama.ecommerceservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImp implements CartService {

    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final CartMapper cartMapper;

    @Override
    public CartDto getCartItems(long userId) {
        Cart cart = cartRepository.findByCustomer_User_Id(userId);
        return cartMapper.mapCartToDto(cart);
    }

    @Override
    public void addItemToShoppingCart(long userId, AddItemToCartDto addItemToCartDto) {

    }

    @Override
    public void updateCartItem(long productId, long userId, short quantity) {

    }

    @Override
    public void removeCartItem(long productId, long userId) {

    }
}
