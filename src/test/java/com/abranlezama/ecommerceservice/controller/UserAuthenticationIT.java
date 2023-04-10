package com.abranlezama.ecommerceservice.controller;

import com.abranlezama.ecommerceservice.config.security.JwtService;
import com.abranlezama.ecommerceservice.dto.authentication.AuthResponseDto;
import com.abranlezama.ecommerceservice.dto.authentication.CustomerRegistrationDto;
import com.abranlezama.ecommerceservice.dto.authentication.AuthRequestDto;
import com.abranlezama.ecommerceservice.model.User;
import com.abranlezama.ecommerceservice.objectmother.CustomerMother;
import com.abranlezama.ecommerceservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.utility.DockerImageName.parse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers(disabledWithoutDocker = true)
class UserAuthenticationIT {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(parse("postgres:15.1"))
            .withDatabaseName("ecommerce")
            .withUsername("test")
            .withUsername("test");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;


    @Test
    @Sql(scripts = "/scripts/INIT_SYSTEM_ROLES.sql")
    void shouldRegisterNewUserAndThenReturnJwtTokenWhenAuthenticating() {
        // Given
        CustomerRegistrationDto request = CustomerMother
                .registrationDto().build();

        AuthRequestDto authRequestDto = AuthRequestDto.builder()
                .email(request.email())
                .password(request.password())
                .build();

        // When
        this.webTestClient
                .post().uri("/api/v1/customer/register")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();

        AuthResponseDto responseDto = this.webTestClient
                .post().uri("/api/v1/login")
                .bodyValue(authRequestDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthResponseDto.class)
                .returnResult().getResponseBody();

        // Then
        assert responseDto != null;
        String tokenEmail = jwtService.extractUsername(responseDto.token());
        User user = userRepository.findByEmail(request.email()).orElseThrow();

        assertThat(user.getEmail()).isEqualTo(request.email());
        assertThat(tokenEmail).isEqualTo(user.getEmail());
    }

}
