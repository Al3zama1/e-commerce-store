package com.abranlezama.ecommerceservice.repository;

import com.abranlezama.ecommerceservice.model.Role;
import com.abranlezama.ecommerceservice.model.RoleType;
import com.abranlezama.ecommerceservice.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.utility.DockerImageName.parse;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers(disabledWithoutDocker = true)
class UserRepositoryTest {

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
    private UserRepository cut;
    @Autowired
    private RoleRepository roleRepository;

    @Test
    @Sql(scripts = "/scripts/INIT_SYSTEM_ROLES.sql")
    void shouldRegisterUserWithTheGivenRoles() {
        // Given
        Set<Role> roles = roleRepository.findByNameIn(Set.of(RoleType.ROLE_EMPLOYEE, RoleType.ROLE_ADMIN));
        User user = User.builder()
                .email("john.last@gmail.com")
                .password("12345678")
                .roles(roles)
                .build();

        // When
        user = cut.saveAndFlush(user);


        // Then
        assertThat(user.getRoles()).isEqualTo(roles);
        System.out.println(user);
        System.out.println(user.getAuthorities());
    }
}
