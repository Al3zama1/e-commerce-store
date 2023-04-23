package com.abranlezama.ecommerceservice.service.imp;

import com.abranlezama.ecommerceservice.dto.order.OrderDto;
import com.abranlezama.ecommerceservice.dto.order.OrderItemDto;
import com.abranlezama.ecommerceservice.exception.EmptyOrderException;
import com.abranlezama.ecommerceservice.exception.ExceptionMessages;
import com.abranlezama.ecommerceservice.mapstruct.mapper.OrderMapper;
import com.abranlezama.ecommerceservice.model.*;
import com.abranlezama.ecommerceservice.repository.*;
import com.abranlezama.ecommerceservice.service.OrderService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.persistence.criteria.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImp implements OrderService {
    private final CartRepository cartRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final OrderMapper orderMapper;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;
    @Value("${stripe.api.key}")
    private String stripe_api_key;
    @Value("${application.base.url}")
    private String baseUrl;


    @Override
    public Session createSession(User user) {
        Cart cart = cartRepository.findByCustomer_User_Id(user.getId());
        String successUrl = baseUrl + "payment/success";
        String failureUrl = baseUrl + "payment/failed";

        Stripe.apiKey = stripe_api_key;
        List<SessionCreateParams.LineItem> sessionItemList = new ArrayList<>();

        for (CartItem cartItem : cart.getCartItems()) {
            sessionItemList.add(createSessionLineItem(cartItem));
        }

        SessionCreateParams params = SessionCreateParams.builder()
                .setCustomerEmail(user.getEmail())
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(failureUrl)
                .addAllLineItem(sessionItemList)
                .build();

        try {
            return Session.create(params);
        } catch (StripeException ex) {
            throw new RuntimeException("something went wrong...");
        }
    }

    @Override
    public long createOrder(long userId) {
        // get users shopping cart
        Cart cart = cartRepository.findByCustomer_User_Id(userId);

        if (cart.getCartItems().size() == 0) throw new EmptyOrderException(ExceptionMessages.EMPTY_ORDER);


        OrderStatus orderStatus = orderStatusRepository.findByStatus(OrderStatusType.PROCESSING)
                .orElseThrow(() -> new RuntimeException("Order status does not exists"));

        CustomerOrder order = CustomerOrder.builder()
                .customer(cart.getCustomer())
                .orderStatus(orderStatus)
                .datePlaced(LocalDateTime.now())
                .total(cart.getTotalCost())
                .build();

        order = customerOrderRepository.save(order);


        orderItemRepository.saveAll(createOrderItems(cart.getCartItems(), order));
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.setTotalCost(0F);
        cart.getCartItems().removeAll(cart.getCartItems());
        cartRepository.save(cart);

        return order.getId();
    }

    @Override
    public List<OrderDto> getOrders(long userId) {
        List<CustomerOrder> customerOrders = customerOrderRepository.findAllByCustomer_Id(userId);

        return customerOrders.stream().map(orderMapper::mapOrderToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderItemDto> getOrder(long orderId, long userId) {
        return null;
    }

    private List<OrderItem> createOrderItems(List<CartItem> cartItems, CustomerOrder customerOrder) {
        return cartItems.stream().map(item -> OrderItem.builder()
                .product(item.getProduct())
                .quantity(item.getQuantity())
                .order(customerOrder)
                .price(item.getProduct().getPrice())
                .build())
                .collect(Collectors.toList());
    }


    private SessionCreateParams.LineItem createSessionLineItem(CartItem cartItem) {
        return SessionCreateParams.LineItem.builder()
                .setPriceData(createPriceData(cartItem))
                .setQuantity((long)cartItem.getQuantity())
                .build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(CartItem cartItem) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmount((long) (cartItem.getProduct().getPrice() * 100))
                .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(cartItem.getProduct().getName())
                                .build()
                ).build();
    }
}
