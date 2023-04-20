package com.abranlezama.ecommerceservice.service.imp;

import com.abranlezama.ecommerceservice.dto.cart.AddItemToCartDto;
import com.abranlezama.ecommerceservice.exception.ExceptionMessages;
import com.abranlezama.ecommerceservice.exception.ProductNotFoundException;
import com.abranlezama.ecommerceservice.exception.ProductOutOfStockException;
import com.abranlezama.ecommerceservice.mapstruct.mapper.CartMapper;
import com.abranlezama.ecommerceservice.model.Cart;
import com.abranlezama.ecommerceservice.model.CartItem;
import com.abranlezama.ecommerceservice.model.CartItemPK;
import com.abranlezama.ecommerceservice.model.Product;
import com.abranlezama.ecommerceservice.objectmother.CartMother;
import com.abranlezama.ecommerceservice.objectmother.ProductMother;
import com.abranlezama.ecommerceservice.repository.CartItemRepository;
import com.abranlezama.ecommerceservice.repository.CartRepository;
import com.abranlezama.ecommerceservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(SpringExtension.class)
class CartServiceImpTest {
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CartMapper cartMapper;

    @InjectMocks
    private CartServiceImp cut;
    @Captor
    private ArgumentCaptor<CartItem> cartItemArgumentCaptor;
    @Captor
    private ArgumentCaptor<Cart> cartArgumentCaptor;

    @Test
    void shouldReturnCustomerCartItems() {
        // Given
        long userId = 1L;

        // When
        cut.getCartItems(userId);

        // When
        then(cartRepository).should().findByCustomer_User_Id(userId);
    }


    @Test
    void shouldAddProductToUserShoppingCart() {
        // Given
        long userId = 1L;
        AddItemToCartDto addItemToCartDto = AddItemToCartDto.builder()
                .productId(1L).quantity((short) 2)
                .build();
        Product product = ProductMother.saveProduct().id(addItemToCartDto.productId()).build();
        Cart cart = CartMother.cart().totalCost(500F).cartItems(new ArrayList<>()).build();

        given(cartRepository.findByCustomer_User_Id(userId)).willReturn(cart);
        given(productRepository.findById(addItemToCartDto.productId())).willReturn(Optional.of(product));

        // When
        cut.addItemToShoppingCart(userId, addItemToCartDto);

        // Then
        then(cartRepository).should().save(cartArgumentCaptor.capture());
        assertThat(cartArgumentCaptor.getValue().getTotalCost()).isEqualTo(1000F);
    }

    @Test
    void shouldThrowProductNotFoundExceptionWhenAddingNonExistingProductToCart() {
        // Given
        long userId = 1L;
        AddItemToCartDto addItemToCartDto = AddItemToCartDto.builder()
                .productId(1L).quantity((short) 2)
                .build();

        given(cartRepository.findByCustomer_User_Id(userId)).willReturn(new Cart());
        given(productRepository.findById(addItemToCartDto.productId())).willReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> cut.addItemToShoppingCart(userId, addItemToCartDto))
                .hasMessage(ExceptionMessages.PRODUCT_NOT_FOUND)
                .isInstanceOf(ProductNotFoundException.class);

        // Then
        then(cartItemRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldIncrementCartItemProductQuantityWhenItIsAlreadyInCustomerCart() {
        // Given
        long userId = 1L;
        AddItemToCartDto addItemToCartDto = AddItemToCartDto.builder()
                .productId(1L).quantity((short) 2)
                .build();
        Product product = ProductMother.saveProduct().id(addItemToCartDto.productId()).build();
        Cart cart = CartMother.cart().totalCost(500F).build();
        CartItem cartItem = CartMother.cartItem().product(product).cart(cart).build();
        cart.setCartItems(List.of(cartItem));


        given(cartRepository.findByCustomer_User_Id(userId)).willReturn(cart);
        given(productRepository.findById(addItemToCartDto.productId())).willReturn(Optional.of(product));

        // When
        cut.addItemToShoppingCart(userId, addItemToCartDto);

        // Then
        then(cartRepository).should().save(cartArgumentCaptor.capture());
        assertThat(cartArgumentCaptor.getValue().getTotalCost()).isEqualTo(1500F);
        assertThat(cart.getCartItems().get(0).getQuantity()).isEqualTo((short) 3);
    }

    @Test
    void shouldThrowProductOutOfStockExceptionWhenWantedQuantityIsGreaterThanAvailable() {
        // Given
        long userId = 1L;
        AddItemToCartDto addItemToCartDto = AddItemToCartDto.builder()
                .productId(1L).quantity((short) 2).build();
        Product product = ProductMother.saveProduct().id(addItemToCartDto.productId()).stockQuantity(1).build();
        Cart cart = CartMother.cart().totalCost(500F).build();
        CartItem cartItem = CartMother.cartItem().product(product).cart(cart).build();
        cart.setCartItems(List.of(cartItem));


        given(cartRepository.findByCustomer_User_Id(userId)).willReturn(cart);
        given(productRepository.findById(addItemToCartDto.productId())).willReturn(Optional.of(product));

        // When
        assertThatThrownBy(() -> cut.addItemToShoppingCart(userId, addItemToCartDto))
                .hasMessage(ExceptionMessages.PRODUCT_OUT_OF_STOCK)
                .isInstanceOf(ProductOutOfStockException.class);

        // Then
        then(cartRepository).should(never()).save(any());
    }

}
