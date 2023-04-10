package com.abranlezama.ecommerceservice.objectmother;

import com.abranlezama.ecommerceservice.dto.authentication.AuthRequestDto;
import com.abranlezama.ecommerceservice.model.User;

public class UserMother {

    public static User.UserBuilder user() {
        return User.builder()
                .email("john.last@gmail.com")
                .password("%W0rldFine001");
    }

    public static AuthRequestDto.AuthRequestDtoBuilder userAuthRequest() {
        return AuthRequestDto.builder()
                .email("john.last@gmail.com")
                .password("%W0rldFine001");
    }
}
