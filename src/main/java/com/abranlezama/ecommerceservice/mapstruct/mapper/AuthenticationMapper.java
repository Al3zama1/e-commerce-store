package com.abranlezama.ecommerceservice.mapstruct.mapper;

import com.abranlezama.ecommerceservice.dto.authentication.CustomerRegistrationDto;
import com.abranlezama.ecommerceservice.model.Customer;
import com.abranlezama.ecommerceservice.model.Role;
import com.abranlezama.ecommerceservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper
public interface AuthenticationMapper {


    User mapCustomerRegistrationDtoToUser(CustomerRegistrationDto requestDto, Set<Role> roles);

    @Mapping(target = "id", ignore = true)
    Customer mapCustomerRegistrationDtoToCustomer(CustomerRegistrationDto requestDto, User user);
}
