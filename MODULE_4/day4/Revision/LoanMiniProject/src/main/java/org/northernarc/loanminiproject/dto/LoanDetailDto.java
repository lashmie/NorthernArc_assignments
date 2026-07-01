package org.northernarc.loanminiproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanDetailDto {
    private Long loanId;
    private String customerName;
    private Double principalAmount;
    private Double annualInterestRate;
    private String loanStatus;
    private Double outstandingAmount;
}

