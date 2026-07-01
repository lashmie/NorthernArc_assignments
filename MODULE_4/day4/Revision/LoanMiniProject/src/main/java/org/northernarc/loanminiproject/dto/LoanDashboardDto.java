package org.northernarc.loanminiproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanDashboardDto {
    private Long totalLoans;
    private Double totalPrincipalAmount;
    private Double averageInterestRate;
    private Long activeLoans;
    private Long closedLoans;
    private Long defaultedLoans;
    private Double overallCollectionAmount;
    private Long pendingEmis;
    private Long overdueEmis;
}

