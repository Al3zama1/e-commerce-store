package com.abranlezama.ecommerceservice.service;

import com.abranlezama.ecommerceservice.dto.order.OrderDto;
import com.abranlezama.ecommerceservice.model.User;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

import java.util.List;

public interface OrderService {

    Session createSession(User user);
    long createOrder(long userId);
    List<OrderDto> getOrders(long userId);
}
