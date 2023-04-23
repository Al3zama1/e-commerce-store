package com.abranlezama.ecommerceservice.service;

import com.abranlezama.ecommerceservice.model.User;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

public interface OrderService {

    Session createSession(User user);
    long createOrder(long userId);
}
