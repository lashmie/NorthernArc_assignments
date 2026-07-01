package org.northernarc.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDashboardDTO {

    private long totalLoans;
    private long activeLoans;
    private long closedLoans;
    private long overdueEmis;
    private Double totalEmiCollected;
    private Double totalPenaltyCollected;
}

