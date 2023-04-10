package com.abranlezama.ecommerceservice.service.imp;

import com.abranlezama.ecommerceservice.config.security.JwtService;
import com.abranlezama.ecommerceservice.dto.authentication.AuthResponseDto;
import com.abranlezama.ecommerceservice.dto.authentication.CustomerRegistrationDto;
import com.abranlezama.ecommerceservice.dto.authentication.AuthRequestDto;
import com.abranlezama.ecommerceservice.exception.ExceptionMessages;
import com.abranlezama.ecommerceservice.exception.PasswordException;
import com.abranlezama.ecommerceservice.exception.UserException;
import com.abranlezama.ecommerceservice.exception.UsernameTakenException;
import com.abranlezama.ecommerceservice.mapstruct.mapper.AuthenticationMapper;
import com.abranlezama.ecommerceservice.model.Customer;
import com.abranlezama.ecommerceservice.model.Role;
import com.abranlezama.ecommerceservice.model.RoleType;
import com.abranlezama.ecommerceservice.model.User;
import com.abranlezama.ecommerceservice.repository.CustomerRepository;
import com.abranlezama.ecommerceservice.repository.RoleRepository;
import com.abranlezama.ecommerceservice.repository.UserRepository;
import com.abranlezama.ecommerceservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImp  implements AuthenticationService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationMapper authenticationMapper;

    @Override
    public AuthResponseDto login(AuthRequestDto authRequestDto) {
        // Check user exists
        Optional<User> userOptional = userRepository.findByEmail(authRequestDto.email());

        if (userOptional.isEmpty()) throw new UserException(ExceptionMessages.FAILED_AUTHENTICATION);

        // Compare passwords
        if (!passwordEncoder.matches(authRequestDto.password(), userOptional.get().getPassword())) {
            throw new UserException(ExceptionMessages.FAILED_AUTHENTICATION);
        }

        // Generate token for user
        String jwtToken = jwtService.generateToken(userOptional.get());

        return AuthResponseDto.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public void registerCustomer(CustomerRegistrationDto registrationDto) {
        // retrieve roles to be assigned to customer
        Set<Role> roles = roleRepository.findByNameIn(Set.of(RoleType.ROLE_CUSTOMER));

        // verify user with given email does not exist
        if (userRepository.existsByEmail(registrationDto.email()))
            throw new UsernameTakenException(ExceptionMessages.USER_ALREADY_EXISTS);

        // verify both passwords match
        if (!registrationDto.password().equals(registrationDto.confirmPassword()))
            throw new PasswordException(ExceptionMessages.PASSWORDS_MUST_MATCH);

        // map and save user
        User user = authenticationMapper.mapCustomerRegistrationDtoToUser(registrationDto, roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);

        // map and save customer
        Customer customer = authenticationMapper.mapCustomerRegistrationDtoToCustomer(registrationDto, user);
        customerRepository.save(customer);
    }


}
