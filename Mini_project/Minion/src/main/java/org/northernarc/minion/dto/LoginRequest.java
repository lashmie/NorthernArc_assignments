package org.northernarc.minion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Login request DTO for user authentication
 */
@Schema(description = "Login request containing email and password credentials")
public record LoginRequest(
        @Schema(description = "User email address", example = "user@example.com")
        @NotBlank @Email String email,

        @Schema(description = "User password", example = "SecurePassword123!")
        @NotBlank String password
) {
    private static final Logger logger = LoggerFactory.getLogger(LoginRequest.class);

    static {
        logger.info("LoginRequest record initialized");
    }
}

