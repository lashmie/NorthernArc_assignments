package org.northernarc.productrental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRentalSummaryDTO {

    private String customerName;
    private String city;
    private Long numberOfRentals;
    private Double totalRentPaid;
}

