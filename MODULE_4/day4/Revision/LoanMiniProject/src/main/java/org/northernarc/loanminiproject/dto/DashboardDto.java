package org.northernarc.loanminiproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDto {
    private Long totalCustomers;
    private Long totalLoans;
    private Long activeLoans;
    private Long closedLoans;
    private Long defaultedLoans;
    private Long overdueEmis;
    private Double totalEmiCollected;
    private Double totalPenaltyCollected;
    private Double averageInterestRate;
    private LoanDetailDto highestOutstandingLoan;
    private CustomerDetailDto highestPayingCustomer;
    private Long npaAccounts;
}

