package com.abranlezama.ecommerceservice.mapstruct.mapper;

import com.abranlezama.ecommerceservice.dto.authentication.CustomerRegistrationDto;
import com.abranlezama.ecommerceservice.model.Customer;
import com.abranlezama.ecommerceservice.model.Role;
import com.abranlezama.ecommerceservice.model.RoleType;
import com.abranlezama.ecommerceservice.model.User;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationMapperTest {

    private static final Faker FAKER = new Faker();
    private static final AuthenticationMapper INSTANCE = Mappers.getMapper(AuthenticationMapper.class);

    @Test
    void shouldMapCustomerRegistrationDtoToUser() {
        // Given
        Role customerRole = Role.builder().name(RoleType.ROLE_CUSTOMER).build();
        CustomerRegistrationDto customerRegistrationDto = CustomerRegistrationDto.builder()
                .email(FAKER.internet().emailAddress())
                .password(FAKER.internet().password())
                .build();

        // When
        User user = INSTANCE.mapCustomerRegistrationDtoToUser(customerRegistrationDto, Set.of(customerRole));

        // Then
        assertThat(customerRegistrationDto.email()).isEqualTo(user.getEmail());
        assertThat(customerRegistrationDto.password()).isEqualTo(user.getPassword());
        assertThat(Set.of(customerRole)).isEqualTo(user.getRoles());
    }

    @Test
    void shouldMapCustomerRegistrationDtoToCustomer() {
        // Given
        Role customerRole = Role.builder().name(RoleType.ROLE_CUSTOMER).build();
        CustomerRegistrationDto customerRegistrationDto = CustomerRegistrationDto.builder()
                .firstName(FAKER.name().firstName())
                .lastName(FAKER.name().lastName())
                .phone(FAKER.phoneNumber().cellPhone())
                .dateOfBirth(LocalDate.of(2000, 10, 11))
                .street(FAKER.address().streetAddress())
                .city(FAKER.address().city())
                .region(FAKER.address().state())
                .postalCode(FAKER.address().zipCode())
                .country(FAKER.address().country())
                .build();

        // When
        Customer customer = INSTANCE.mapCustomerRegistrationDtoToCustomer(customerRegistrationDto, null);

        // Then
        assertThat(customer.getFirstName()).isEqualTo(customerRegistrationDto.firstName());
        assertThat(customer.getLastName()).isEqualTo(customerRegistrationDto.lastName());
        assertThat(customer.getPhone()).isEqualTo(customerRegistrationDto.phone());
        assertThat(customer.getDateOfBirth()).isEqualTo(customerRegistrationDto.dateOfBirth());
        assertThat(customer.getStreet()).isEqualTo(customerRegistrationDto.street());
        assertThat(customer.getCity()).isEqualTo(customerRegistrationDto.city());
        assertThat((customer.getRegion())).isEqualTo(customerRegistrationDto.region());
        assertThat(customer.getPostalCode()).isEqualTo(customerRegistrationDto.postalCode());
        assertThat(customer.getCountry()).isEqualTo(customerRegistrationDto.country());
        assertThat(customer.getUser()).isNull();

    }

}
