package com.abranlezama.ecommerceservice.mapstruct.mapper;

import com.abranlezama.ecommerceservice.dto.cart.CartDto;
import com.abranlezama.ecommerceservice.dto.cart.CartItemDto;
import com.abranlezama.ecommerceservice.model.Cart;
import com.abranlezama.ecommerceservice.model.CartItem;
import org.mapstruct.Mapper;

@Mapper(
        uses = {ProductMapper.class}
)
public interface CartMapper {

    CartItemDto mapCartItemToDto(CartItem cartItem);

    CartDto mapCartToDto(Cart cart);

}
