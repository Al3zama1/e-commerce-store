package com.abranlezama.ecommerceservice.service.imp;

import com.abranlezama.ecommerceservice.dto.cart.AddItemToCartDto;
import com.abranlezama.ecommerceservice.dto.cart.CartDto;
import com.abranlezama.ecommerceservice.exception.ExceptionMessages;
import com.abranlezama.ecommerceservice.exception.ProductNotFoundException;
import com.abranlezama.ecommerceservice.exception.ProductOutOfStockException;
import com.abranlezama.ecommerceservice.mapstruct.mapper.CartMapper;
import com.abranlezama.ecommerceservice.model.*;
import com.abranlezama.ecommerceservice.repository.CartItemRepository;
import com.abranlezama.ecommerceservice.repository.CartRepository;
import com.abranlezama.ecommerceservice.repository.CustomerRepository;
import com.abranlezama.ecommerceservice.repository.ProductRepository;
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
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartDto getCartItems(long userId) {
        Cart cart = cartRepository.findByCustomer_User_Id(userId);
        return cartMapper.mapCartToDto(cart);
    }

    @Override
    public void addItemToShoppingCart(long userId, AddItemToCartDto addItemToCartDto) {
        // Get customer's shopping cart
        Cart cart = cartRepository.findByCustomer_User_Id(userId);
        // Get product that will be added to cart
        Product product = productRepository.findById(addItemToCartDto.productId())
                .orElseThrow(() -> new ProductNotFoundException(ExceptionMessages.PRODUCT_NOT_FOUND));

        // check if product is already in customer's cart
        Optional<CartItem> cartItemOptional = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(product.getId()))
                .findFirst();

        CartItem cartItem;

        if (cartItemOptional.isPresent()) {
            cartItem = cartItemOptional.get();
            cartItem.setQuantity((short) (cartItem.getQuantity() + addItemToCartDto.quantity()));
        } else {
            cartItem = CartItem.builder().cart(cart).quantity(addItemToCartDto.quantity())
                    .product(product).build();
            cart.getCartItems().add(cartItem);
        }

        // verify wanted quantity is in stock
        if (productOutOfStock(product, cartItem.getQuantity())) throw new
                ProductOutOfStockException(ExceptionMessages.PRODUCT_OUT_OF_STOCK);

        // calculate cart total
        cart.setTotalCost(calculateCartTotal(cart));

        cartRepository.save(cart);
    }

    private float calculateCartTotal(Cart cart) {
        return cart.getCartItems().stream()
                .map(item -> item.getProduct().getPrice() * item.getQuantity())
                .reduce(0F, Float::sum);
    }

    private boolean productOutOfStock(Product product, short wanted) {
        return product.getStockQuantity() < wanted;
    }

    @Override
    public void updateCartItem(long productId, long userId, short quantity) {
        // get customer's shopping cart
        Cart cart = cartRepository.findByCustomer_User_Id(userId);
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException(ExceptionMessages.PRODUCT_NOT_IN_CART));

        cartItem.setQuantity(quantity);
        if (productOutOfStock(cartItem.getProduct(), quantity)) throw new
                ProductOutOfStockException(ExceptionMessages.PRODUCT_OUT_OF_STOCK);

        cart.setTotalCost(calculateCartTotal(cart));

        cartRepository.save(cart);
    }

    @Override
    public void removeCartItem(long productId, long userId) {
        // obtain customer's shopping cart
        Cart cart = cartRepository.findByCustomer_User_Id(userId);
        // find and remove item with given productId
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException(ExceptionMessages.PRODUCT_NOT_IN_CART));
        cart.getCartItems().remove(cartItem);

        // update cart total
        cart.setTotalCost(calculateCartTotal(cart));

        cartRepository.save(cart);
    }
}
