package org.northernarc.minion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loan summary DTO with essential loan details
 */
@Schema(description = "Summary information for a loan")
public record LoanSummaryDTO(
        @Schema(description = "Loan ID", example = "1")
        Long loanId,

        @Schema(description = "Type of loan", example = "PERSONAL_LOAN")
        String loanType,

        @Schema(description = "Principal loan amount", example = "100000.00")
        Double principalAmount,

        @Schema(description = "Annual interest rate percentage", example = "8.5")
        Double annualInterestRate,

        @Schema(description = "Loan tenure in months", example = "24")
        Integer tenureMonths,

        @Schema(description = "Monthly EMI amount", example = "4500.00")
        Double emiAmount,

        @Schema(description = "Loan disbursement date")
        LocalDate disbursementDate,

        @Schema(description = "Current loan status (ACTIVE, CLOSED, DEFAULTED)", example = "ACTIVE")
        String loanStatus,

        @Schema(description = "Customer name", example = "John Doe")
        String customerName,

        @Schema(description = "Customer city", example = "Mumbai")
        String city
) {
    private static final Logger logger = LoggerFactory.getLogger(LoanSummaryDTO.class);

    static {
        logger.info("LoanSummaryDTO record initialized");
    }
}

