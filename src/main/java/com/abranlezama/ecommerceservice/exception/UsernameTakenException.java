package com.abranlezama.ecommerceservice.exception;

public class UsernameTakenException extends RuntimeException{

    public UsernameTakenException(String message) {
        super(message);
    }
}
