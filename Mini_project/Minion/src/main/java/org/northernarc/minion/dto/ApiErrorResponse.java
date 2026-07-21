package org.northernarc.minion.dto;

import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * API error response DTO for error handling
 */
public record ApiErrorResponse(
        Instant timestamp,

        int status,

        String error,

        String message,

        String path
) {
    private static final Logger logger = LoggerFactory.getLogger(ApiErrorResponse.class);

    static {
        logger.info("ApiErrorResponse record initialized");
    }
}

