package org.northernarc.loanminiproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanSummaryDto {
    private Long loanId;
    private String loanType;
    private Double principalAmount;
    private Double annualInterestRate;
    private Integer tenureMonths;
    private Double emiAmount;
    private String loanStatus;
    private LocalDate disbursementDate;
    private String customerName;
    private String email;
    private String phoneNumber;
}

