package org.northernarc.loan.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDate;

@Getter
@Setter
public class LoanApprovalRequest {

    @NotNull(message = "Customer id is required")
    private Long customerId;

    @NotBlank(message = "Loan type is required")
    private String loanType;

    @NotNull(message = "Principal amount is required")
    @DecimalMin(value = "0.01", message = "Principal amount must be greater than zero")
    @Digits(integer = 15, fraction = 2)
    private Double principalAmount;

    @NotNull(message = "Annual interest rate is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Annual interest rate cannot be negative")
    @Digits(integer = 3, fraction = 2)
    private Double annualInterestRate;

    @NotNull(message = "Tenure is required")
    @Min(value = 1, message = "Tenure must be at least 1 month")
    private BigInteger tenureMonths;

    @NotNull(message = "Disbursement date is required")
    private LocalDate disbursementDate;
}

