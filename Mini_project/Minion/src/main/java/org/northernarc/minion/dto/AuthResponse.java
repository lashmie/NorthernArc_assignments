package org.northernarc.minion.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Authentication response DTO containing JWT token and user information
 */
public record AuthResponse(
        String token,

        long expiresAtEpochMillis,

        String role
) {
    private static final Logger logger = LoggerFactory.getLogger(AuthResponse.class);

    static {
        logger.info("AuthResponse record initialized");
    }
}

