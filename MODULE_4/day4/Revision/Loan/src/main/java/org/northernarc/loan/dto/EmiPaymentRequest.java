package org.northernarc.loan.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EmiPaymentRequest {

    @NotNull(message = "EMI id is required")
    private Long emiId;

    @NotNull(message = "Payment amount is required")
    @DecimalMin(value = "0.01", message = "Payment amount must be greater than zero")
    @Digits(integer = 15, fraction = 2)
    private Double amount;

    @NotBlank(message = "Payment mode is required")
    private String paymentMode;

    @NotBlank(message = "Reference number is required")
    private String referenceNumber;

    private LocalDate paymentDate;
}

