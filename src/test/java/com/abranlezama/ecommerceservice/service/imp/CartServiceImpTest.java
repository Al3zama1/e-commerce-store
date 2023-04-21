package com.abranlezama.ecommerceservice.service.imp;

import com.abranlezama.ecommerceservice.exception.ExceptionMessages;
import com.abranlezama.ecommerceservice.exception.ProductNotFoundException;
import com.abranlezama.ecommerceservice.exception.ProductOutOfStockException;
import com.abranlezama.ecommerceservice.mapstruct.mapper.CartMapper;
import com.abranlezama.ecommerceservice.model.Cart;
import com.abranlezama.ecommerceservice.model.CartItem;
import com.abranlezama.ecommerceservice.model.Product;
import com.abranlezama.ecommerceservice.objectmother.CartItemMother;
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
        long productId = 1L;
        short quantity = 2;

        Product product = ProductMother.saveProduct()
                .id(productId)
                .build();
        Cart cart = CartMother.cart()
                .cartItems(new ArrayList<>())
                .build();

        given(cartRepository.findByCustomer_User_Id(userId)).willReturn(cart);
        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        // When
        cut.addItemToShoppingCart(userId, productId, quantity);

        // Then
        then(cartRepository).should().save(cartArgumentCaptor.capture());
        assertThat(cartArgumentCaptor.getValue().getTotalCost()).isEqualTo(2 * product.getPrice());
    }

    @Test
    void shouldThrowProductNotFoundExceptionWhenAddingNonExistingProductToCart() {
        // Given
        long userId = 1L;
        long productId = 1L;
        short quantity = 2;

        given(cartRepository.findByCustomer_User_Id(userId)).willReturn(new Cart());
        given(productRepository.findById(productId)).willReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> cut.addItemToShoppingCart(userId, productId, quantity))
                .hasMessage(ExceptionMessages.PRODUCT_NOT_FOUND)
                .isInstanceOf(ProductNotFoundException.class);

        // Then
        then(cartItemRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldIncrementCartItemProductQuantityWhenItIsAlreadyInCustomerCart() {
        // Given
        long userId = 1L;
        long productId = 1L;
        short quantity = 2;

        Product product = ProductMother.saveProduct()
                .id(productId)
                .build();
        Cart cart = CartMother.cart()
                .totalCost(500F)
                .cartItems(List.of(
                        CartItemMother.cartItem().product(product).build()
                ))
                .build();

        given(cartRepository.findByCustomer_User_Id(userId)).willReturn(cart);
        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        // When
        cut.addItemToShoppingCart(userId, productId, quantity);

        // Then
        then(cartRepository).should().save(cartArgumentCaptor.capture());
        assertThat(cartArgumentCaptor.getValue().getTotalCost()).isEqualTo(3 * product.getPrice());
        assertThat(cart.getCartItems().get(0).getQuantity()).isEqualTo((short) 3);
    }

    @Test
    void shouldThrowProductOutOfStockExceptionWhenWantedQuantityIsGreaterThanAvailable() {
        // Given
        long userId = 1L;
        long productId = 1L;
        short quantity = 2;

        Product product = ProductMother.saveProduct()
                .id(productId)
                .stockQuantity(1)
                .build();
        Cart cart = CartMother.cart().totalCost(500F)
                .cartItems(List.of(
                        CartItemMother.cartItem().product(product).build()
                ))
                .build();

        given(cartRepository.findByCustomer_User_Id(userId)).willReturn(cart);
        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        // When
        assertThatThrownBy(() -> cut.addItemToShoppingCart(userId, productId, quantity))
                .hasMessage(ExceptionMessages.PRODUCT_OUT_OF_STOCK)
                .isInstanceOf(ProductOutOfStockException.class);

        // Then
        then(cartRepository).should(never()).save(any());
    }

    @Test
    void shouldUpdateCartItemQuantity() {
        // Given
        long productId = 1L;
        long userId = 1L;
        short quantity = 3;

        Product product = ProductMother.saveProduct().id(productId).build();
        Cart cart = CartMother.cart()
                .cartItems(List.of(
                        CartItemMother.cartItem()
                                .product(product)
                                .build()
                ))
                .build();

        given(cartRepository.findByCustomer_User_Id(userId)).willReturn(cart);

        // When
        cut.updateCartItem(productId, userId, quantity);

        // Then
        then(cartRepository).should().save(cartArgumentCaptor.capture());
        assertThat(cartArgumentCaptor.getValue().getCartItems().get(0).getQuantity()).isEqualTo(quantity);
        assertThat(cartArgumentCaptor.getValue().getTotalCost()).isEqualTo(quantity * product.getPrice());
    }

    @Test
    void shouldThrowProductNotFoundExceptionWhenUpdatingProductThatIsNotInShoppingCart() {
        // Given
        long productId = 1L;
        long userId = 1L;
        short quantity = 3;
        Cart cart = CartMother.cart().cartItems(List.of()).build();

        given(cartRepository.findByCustomer_User_Id(userId)).willReturn(cart);

        // When
        assertThatThrownBy(() -> cut.updateCartItem(productId, userId, quantity))
                .hasMessage(ExceptionMessages.PRODUCT_NOT_IN_CART)
                .isInstanceOf(ProductNotFoundException.class);

        // Then
        then(cartRepository).should(never()).save(any());
    }

    @Test
    void shouldThrowProductOutOfStockExceptionWhenWantedQuantityIsMoreThanAvailableStock() {
        // Given
        long productId = 1L;
        long userId = 1L;
        short quantity = 3;

        Cart cart = CartMother.cart()
                .cartItems(List.of(
                        CartItemMother.cartItem().product(ProductMother.saveProduct()
                                .stockQuantity(2)
                                .id(productId).build())
                        .build()
                ))
                .build();

        given(cartRepository.findByCustomer_User_Id(userId)).willReturn(cart);

        // When
        assertThatThrownBy(() -> cut.updateCartItem(productId, userId, quantity))
                .hasMessage(ExceptionMessages.PRODUCT_OUT_OF_STOCK)
                .isInstanceOf(ProductOutOfStockException.class);

        // Then
        then(cartRepository).should(never()).save(any());
    }

    @Test
    void shouldRemoveProductFromCustomerShoppingCart() {
        // Given
        long userId = 1L;
        long productId = 1L;
        Product product = ProductMother.saveProduct().id(productId).build();
        CartItem cartItem = CartItemMother.cartItem()
                .product(product)
                .build();
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);
       Cart cart = CartMother.cart()
               .totalCost(product.getPrice())
               .cartItems(cartItems)
               .build();

        given(cartRepository.findByCustomer_User_Id(userId)).willReturn(cart);

        // When
        cut.removeCartItem(productId, userId);

        // Then
        then(cartItemRepository).should().delete(cartItem);
        then(cartRepository).should().save(cartArgumentCaptor.capture());
        assertThat(cartArgumentCaptor.getValue().getCartItems().size()).isEqualTo(0);
        assertThat(cartArgumentCaptor.getValue().getTotalCost()).isEqualTo(0);

    }

    @Test
    void shouldThrowProductNotFoundExceptionWhenDeletingProductNotInCart() {
        // Given
        long userId = 1L;
        long productId = 1L;

        Cart cart = CartMother.cart().cartItems(List.of()).build();

        given(cartRepository.findByCustomer_User_Id(userId)).willReturn(cart);

        // When
        assertThatThrownBy(() -> cut.removeCartItem(productId, userId))
                .hasMessage(ExceptionMessages.PRODUCT_NOT_IN_CART)
                .isInstanceOf(ProductNotFoundException.class);

        // Then
        then(cartItemRepository).shouldHaveNoInteractions();
        then(cartRepository).should(never()).save(any());
    }

}
