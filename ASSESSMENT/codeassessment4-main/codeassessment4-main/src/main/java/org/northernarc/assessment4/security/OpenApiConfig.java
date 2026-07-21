package org.northernarc.assessment4.security;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI assessmentOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Banking Assessment API")
                        .version("v1")
                        .description("Secure banking APIs with JWT authentication"));
    }
}

