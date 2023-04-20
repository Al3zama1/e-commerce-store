package com.abranlezama.ecommerceservice.mapstruct.mapper;

import com.abranlezama.ecommerceservice.dto.cart.CartDto;
import com.abranlezama.ecommerceservice.dto.product.ProductDto;
import com.abranlezama.ecommerceservice.model.*;
import com.abranlezama.ecommerceservice.objectmother.CartMother;
import com.abranlezama.ecommerceservice.objectmother.CustomerMother;
import com.abranlezama.ecommerceservice.objectmother.ProductMother;
import com.abranlezama.ecommerceservice.objectmother.UserMother;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class CartMapperTest {

    @Mock
    private ProductMapper productMapper;
    @InjectMocks
    private CartMapperImpl cartMapper;


    @Test
    void shouldMapCartEntityToCartDto() {
        // Given
        long userId = 1L;
        User user = UserMother.user().id(1L).build();
        Customer customer = CustomerMother.customer().id(2L).user(user).build();
        Product product = ProductMother.saveProduct().id(1L).price(100F).build();
        CartItem cartItem = CartMother.cartItem().product(product).build();
        Cart cart = CartMother.cart().customer(customer).cartItems(List.of(cartItem))
                .totalCost(100F)
                .build();
        ProductDto productDto = new ProductDto(product.getId(), product.getName(), product.getDescription(), product.getPrice());

        given(productMapper.mapProductToDto(product)).willReturn(productDto);

        // When
        CartDto cartDto = cartMapper.mapCartToDto(cart);

        // Then
        assertThat(cartDto.totalCost()).isEqualTo(cart.getTotalCost());
        assertThat(cartDto.id()).isEqualTo(cart.getId());
        assertThat(cartDto.cartItems().size()).isEqualTo(cart.getCartItems().size());
    }

}
