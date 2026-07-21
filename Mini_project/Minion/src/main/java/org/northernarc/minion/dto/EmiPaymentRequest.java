package org.northernarc.minion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EMI payment request DTO
 */
@Schema(description = "Request to record an EMI payment")
public record EmiPaymentRequest(
        @Schema(description = "Payment amount", example = "5000.00")
        @Positive Double amount,

        @Schema(description = "Payment mode (e.g., BANK_TRANSFER, CASH, CHEQUE)", example = "BANK_TRANSFER")
        @NotBlank String paymentMode,

        @Schema(description = "Payment reference number (optional)", example = "REF123456789")
        String referenceNumber
) {
    private static final Logger logger = LoggerFactory.getLogger(EmiPaymentRequest.class);

    static {
        logger.info("EmiPaymentRequest record initialized");
    }
}

