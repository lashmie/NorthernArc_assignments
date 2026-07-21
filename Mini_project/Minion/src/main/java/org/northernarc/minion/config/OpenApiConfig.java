package org.northernarc.minion.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger Configuration for Minion Application
 * Provides API documentation with JWT authentication support
 */
@Configuration
public class OpenApiConfig {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(OpenApiConfig.class);

    /**
     * Configure OpenAPI specification for Minion Loan Management API
     */
    @Bean
    public OpenAPI customOpenAPI() {
        logger.info("Initializing OpenAPI Configuration for Minion Loan Management System");

        return new OpenAPI()
                .info(new Info()
                        .title("Minion Loan Management API")
                        .version("1.0.0")
                        .description("Comprehensive API for loan management, EMI tracking, and customer management with JWT authentication")
                        .contact(new Contact()
                                .name("NorthernArc")
                                .url("https://northernarc.com")
                                .email("api@northernarc.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer JWT"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer JWT",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT token for API authentication")));
    }
}

