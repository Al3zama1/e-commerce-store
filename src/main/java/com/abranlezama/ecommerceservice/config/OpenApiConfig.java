package com.abranlezama.ecommerceservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ecommerceAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(servers())
                .tags(tags());
    }

    @Bean
    public Info apiInfo() {
        return new Info()
                .title("E-commerce Store")
                .version("1.0")
                .description("E-commerce CRUD application REST API documentation.")
                .contact(contact());
    }

    @Bean
    public Contact contact() {
        return new Contact()
                .name("Abran lezama")
                .email("lezama.abran@gmail.com")
                .url("https://abranlezama.com/");
    }

    @Bean
    public List<Server> servers() {
        return List.of(
                new Server().url("http://localhost:8080").description("Local development server.")
        );
    }

    @Bean
    public List<Tag> tags() {
        return List.of(
                new Tag().name("Products").description("Product endpoints."),
                new Tag().name("Orders").description("Order endpoints."),
                new Tag().name("Cart").description("Shopping cart endpoints."),
                new Tag().name("Employee").description("Employee related tasks endpoints.")
        );
    }
}
