package org.northernarc.productrental.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI Configuration for Swagger Documentation
 * Provides comprehensive API documentation with Spring Doc OpenAPI
 */
@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Product Rental API")
                        .description("Comprehensive REST API for Product Rental Management System\n\n" +
                                "**Features:**\n" +
                                "- Spring Data JPA with complex JPQL queries\n" +
                                "- JWT Authentication & Authorization\n" +
                                "- Role-based access control (ADMIN, MANAGER, USER)\n" +
                                "- Global exception handling\n" +
                                "- Optimized pagination & sorting\n" +
                                "- DTO projection mapping\n" +
                                "- Dashboard with optimized queries\n" +
                                "- Comprehensive input validation\n\n" +
                                "**Authentication:** All endpoints except /auth/* require JWT token in Authorization header")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Product Rental API Support")
                                .email("support@productrental.com")
                                .url("https://productrental.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Token"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Token",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT token")));
    }
}

