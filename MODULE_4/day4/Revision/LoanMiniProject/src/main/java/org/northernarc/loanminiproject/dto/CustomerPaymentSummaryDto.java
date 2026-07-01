package org.northernarc.loanminiproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPaymentSummaryDto {
    private Long customerId;
    private String customerName;
    private LocalDate latestPaymentDate;
}

