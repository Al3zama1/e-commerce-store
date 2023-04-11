package com.abranlezama.ecommerceservice.repository;

import com.abranlezama.ecommerceservice.model.Role;
import com.abranlezama.ecommerceservice.model.RoleType;
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
class RoleRepositoryTest {

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
    private RoleRepository cut;

    @Test
    @Sql(scripts = "/scripts/INIT_SYSTEM_ROLES.sql")
    void shouldReturnAllRolesInList() {
        // Given
        Set<RoleType> roleNames = Set.of(RoleType.EMPLOYEE, RoleType.ADMIN);

        // When
        Set<Role> roles = cut.findByNameIn(roleNames);

        // Then
        assertThat(roles.size()).isEqualTo(2);
    }

    @Test
    @Sql(scripts = "/scripts/INIT_SYSTEM_ROLES.sql")
    void shouldReturnNoRolesWhenNoneMathInList() {
        // Given
        Set<RoleType> roleNames = Set.of();

        // When
        Set<Role> roles = cut.findByNameIn(roleNames);

        // Then
        assertThat(roles.size()).isEqualTo(0);
    }

}
