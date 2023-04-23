package com.abranlezama.ecommerceservice.exception;

public class EmptyOrderException extends RuntimeException{
    public EmptyOrderException(String message) {
        super(message);
    }
}
