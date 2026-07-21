package org.northernarc.minion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loan-focused dashboard DTO
 */
@Schema(description = "Loan dashboard data with collection statistics by city")
public record LoanDashboardDTO(
        @Schema(description = "Number of active loans", example = "200")
        long activeLoans,

        @Schema(description = "Number of loans with zero overdue EMIs", example = "180")
        long zeroOverdueLoans,

        @Schema(description = "Average interest rate percentage", example = "8.5")
        Double averageInterestRate,

        @Schema(description = "Total penalty amount collected", example = "25000.00")
        Double totalPenaltyCollected,

        @Schema(description = "Total EMI amount collected", example = "500000.00")
        Double totalEmiCollected,

        @Schema(description = "Loan with highest outstanding amount")
        LoanSummaryDTO highestOutstandingLoan,

        @Schema(description = "Map of EMI collection amounts by city")
        Map<String, Double> emiCollectionByCity
) {
    private static final Logger logger = LoggerFactory.getLogger(LoanDashboardDTO.class);

    static {
        logger.info("LoanDashboardDTO record initialized");
    }
}

