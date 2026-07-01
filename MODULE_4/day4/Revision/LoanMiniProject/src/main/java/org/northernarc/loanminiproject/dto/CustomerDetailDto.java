package org.northernarc.loanminiproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetailDto {
    private Long customerId;
    private String customerName;
    private String email;
    private String phoneNumber;
    private String city;
    private Integer creditScore;
    private Double totalAmountPaid;
}

