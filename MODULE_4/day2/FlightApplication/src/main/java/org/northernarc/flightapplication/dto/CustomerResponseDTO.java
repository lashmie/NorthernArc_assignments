package org.northernarc.flightapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO {
    private Long customerId;
    //private String phone;
    private String name;
    //private String email;
    //private String address;
    //private String passportNo;
}
