package org.northernarc.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanSummaryDTO {

    private Long loanId;
    private Long customerId;
    private String customerName;
    private String loanType;
    private Double principalAmount;
    private Double annualInterestRate;
    private BigInteger tenureMonths;
    private Double emiAmount;
    private LocalDate disbursementDate;
    private String loanStatus;
    private Long overdueEmiCount;
    private Double outstandingPrincipal;
}

