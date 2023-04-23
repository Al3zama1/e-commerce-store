package com.abranlezama.ecommerceservice.exception;

public class ExceptionMessages {

    private ExceptionMessages() {}
    public static final String FAILED_AUTHENTICATION = "Password or username do not match our records";
    public static final String USER_ALREADY_EXISTS = "Email taken";
    public static final String PASSWORDS_MUST_MATCH = "Passwords must match";
    public static final String PRODUCT_NOT_FOUND = "Product not found";
    public static final String PRODUCT_NOT_IN_CART = "Product is not in cart";
    public static final String PRODUCT_OUT_OF_STOCK = "Sorry, product is out of stock";
    public static final String EMPTY_ORDER = "Cannot create empty order";
}
