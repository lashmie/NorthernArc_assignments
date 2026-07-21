package org.northernarc.minion.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Comprehensive dashboard DTO for admin view
 */
public record DashboardDTO(
        long totalCustomers,

        long totalLoans,

        long activeLoans,

        long closedLoans,

        long overdueEMIs,

        Double totalEMICollected,

        Double totalPenaltyCollected,

        Double averageInterestRate,

        LoanSummaryDTO highestOutstandingLoan,

        String highestPayingCustomer,

        long NPAAccounts
) {
    private static final Logger logger = LoggerFactory.getLogger(DashboardDTO.class);

    static {
        logger.info("DashboardDTO record initialized");
    }
}

