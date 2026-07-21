package org.northernarc.minion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loan creation request DTO
 */
@Schema(description = "Request to create a new loan for a customer")
public record LoanCreateRequest(
        @Schema(description = "Type of loan", example = "PERSONAL_LOAN")
        @NotBlank String loanType,

        @Schema(description = "Principal loan amount", example = "100000.00")
        @Positive Double principalAmount,

        @Schema(description = "Annual interest rate percentage", example = "8.5")
        @Positive Double annualInterestRate,

        @Schema(description = "Loan tenure in months", example = "24")
        @Positive Integer tenureMonths,

        @Schema(description = "Customer ID", example = "1")
        Long customerId
) {
    private static final Logger logger = LoggerFactory.getLogger(LoanCreateRequest.class);

    static {
        logger.info("LoanCreateRequest record initialized");
    }
}

