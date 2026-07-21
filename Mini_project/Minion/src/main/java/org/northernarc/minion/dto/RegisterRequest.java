package org.northernarc.minion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Customer registration request DTO
 */
@Schema(description = "Customer registration request with validation requirements")
public record RegisterRequest(
        @Schema(description = "Customer full name", example = "John Doe")
        @NotBlank String customerName,

        @Schema(description = "Customer email address", example = "john@example.com")
        @NotBlank @Email String email,

        @Schema(description = "Account password", example = "SecurePassword123!")
        @NotBlank String password,

        @Schema(description = "Customer mobile number (10 digits starting with 6-9)", example = "9876543210")
        @NotBlank @Pattern(regexp = "^[6-9]\\d{9}$") String phoneNumber,

        @Schema(description = "Customer city", example = "Mumbai")
        @NotBlank String city
) {
    private static final Logger logger = LoggerFactory.getLogger(RegisterRequest.class);

    static {
        logger.info("RegisterRequest record initialized");
    }
}

