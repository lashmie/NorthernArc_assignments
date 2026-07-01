package org.northernarc.loanminiproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class LoanCreateRequest {

    @NotBlank(message = "Loan type is required")
    private String loanType;

    @NotNull
    @Positive
    private Double principalAmount;

    @NotNull
    @Positive
    private Double annualInterestRate;

    @NotNull
    @Positive
    private Integer tenureMonths;

    @NotNull
    private Long customerId;
}

