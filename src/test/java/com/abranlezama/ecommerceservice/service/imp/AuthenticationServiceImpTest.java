package com.abranlezama.ecommerceservice.service.imp;

import com.abranlezama.ecommerceservice.config.security.JwtService;
import com.abranlezama.ecommerceservice.dto.authentication.AuthRequestDto;
import com.abranlezama.ecommerceservice.dto.authentication.CustomerRegistrationDto;
import com.abranlezama.ecommerceservice.exception.ExceptionMessages;
import com.abranlezama.ecommerceservice.exception.PasswordException;
import com.abranlezama.ecommerceservice.exception.UserException;
import com.abranlezama.ecommerceservice.exception.UsernameTakenException;
import com.abranlezama.ecommerceservice.mapstruct.mapper.AuthenticationMapper;
import com.abranlezama.ecommerceservice.model.Customer;
import com.abranlezama.ecommerceservice.model.Role;
import com.abranlezama.ecommerceservice.model.RoleType;
import com.abranlezama.ecommerceservice.model.User;
import com.abranlezama.ecommerceservice.objectmother.CustomerMother;
import com.abranlezama.ecommerceservice.objectmother.UserMother;
import com.abranlezama.ecommerceservice.repository.CartRepository;
import com.abranlezama.ecommerceservice.repository.CustomerRepository;
import com.abranlezama.ecommerceservice.repository.RoleRepository;
import com.abranlezama.ecommerceservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(SpringExtension.class)
class AuthenticationServiceImpTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationMapper authenticationMapper;
    @Mock
    JwtService jwtService;

    @InjectMocks
    private AuthenticationServiceImp cut;

    @Test
    void shouldRegisterUserAndThenCreateCustomer() {
        // Given
        CustomerRegistrationDto dto = CustomerMother.registrationDto().build();
        Role role = Role.builder().name(RoleType.CUSTOMER).build();
        Set<Role> roles = Set.of(role);
        User user = UserMother.user().roles(roles).build();
        Customer customer = CustomerMother.customer()
                .user(user).build();


        given(roleRepository.findByNameIn(Set.of(RoleType.CUSTOMER))).willReturn(roles);
        given(userRepository.findByEmail(dto.email())).willReturn(Optional.empty());
        given(passwordEncoder.matches(dto.password(), dto.confirmPassword())).willReturn(true);
        given(authenticationMapper.mapCustomerRegistrationDtoToUser(dto, roles)).willReturn(user);
        given(authenticationMapper.mapCustomerRegistrationDtoToCustomer(dto, user)).willReturn(customer);
        given(userRepository.save(user)).willAnswer(invocation ->{
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        // When
        cut.registerCustomer(dto);

        // Then
        then(userRepository).should().save(user);
        then(customerRepository).should().save(customer);
    }

    @Test
    void shouldThrowUsernameTakenExceptionWhenProvidedEmailIsTaken() {
        // Given
        CustomerRegistrationDto registrationDto = CustomerMother.registrationDto().build();
        given(userRepository.existsByEmail(registrationDto.email())).willReturn(true);

        // When
        assertThatThrownBy(() -> cut.registerCustomer(registrationDto))
                .withFailMessage(ExceptionMessages.FAILED_AUTHENTICATION)
                .isInstanceOf(UsernameTakenException.class);

        // Then
        then(userRepository).should(never()).save(any());
        then(customerRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowPasswordExceptionWhenProvidedPasswordsDoNotMatch() {
        // Given
        CustomerRegistrationDto registrationDto = CustomerMother
                .registrationDto()
                .confirmPassword("randompassword")
                .build();
        given(userRepository.existsByEmail(registrationDto.email())).willReturn(false);


        // When
        assertThatThrownBy(() -> cut.registerCustomer(registrationDto))
                .withFailMessage(ExceptionMessages.PASSWORDS_MUST_MATCH)
                .isInstanceOf(PasswordException.class);

        // Then
        then(userRepository).should(never()).save(any());
        then(customerRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldAuthenticateUserWhenCredentialsAreCorrect() {
        // Given
        AuthRequestDto authRequestDto = UserMother.userAuthRequest().build();
        User user = UserMother.user().build();

        given(userRepository.findByEmail(authRequestDto.email())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(authRequestDto.password(), user.getPassword())).willReturn(true);
        given(jwtService.generateToken(user)).willReturn("token");

        // When
        cut.login(authRequestDto);

        // Then
        then(jwtService).should().generateToken(user);
    }

   @Test
   void shouldThrowUserExceptionWhenUserWithEmailDoesNotExist() {
        // Given
       AuthRequestDto authRequestDto = UserMother.userAuthRequest().build();
       given(userRepository.findByEmail(authRequestDto.email())).willReturn(Optional.empty());

       // When
       assertThatThrownBy(() -> cut.login(authRequestDto))
               .withFailMessage(ExceptionMessages.FAILED_AUTHENTICATION)
               .isInstanceOf(UserException.class);

       // Then
       then(jwtService).shouldHaveNoInteractions();
   }

   @Test
    void shouldThrowUserExceptionWhenPasswordIsIncorrect() {
       // Given
       AuthRequestDto authRequestDto = UserMother.userAuthRequest().build();
       User user = UserMother.user().build();

       given(userRepository.findByEmail(authRequestDto.email())).willReturn(Optional.of(user));

       // When
       assertThatThrownBy(() -> cut.login(authRequestDto))
               .withFailMessage(ExceptionMessages.FAILED_AUTHENTICATION)
               .isInstanceOf(UserException.class);

       // Then
       then(jwtService).shouldHaveNoInteractions();
   }

}
