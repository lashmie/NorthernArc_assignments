package org.northernarc.flightapplication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequestDTO {
    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^\\+?[1-9][0-9]{7,14}$",
            message = "Phone number must be 8-15 digits, optional leading +"
    )
    private String phone;
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 80, message = "Name must be between 2 and 80 characters")
    private String name;
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    @NotBlank(message = "Address is required")
    @Size(min = 5, max = 200, message = "Address must be between 5 and 200 characters")
    private String address;
    @NotBlank(message = "Passport number is required")
    @Pattern(
            regexp = "^[A-Z0-9]{6,9}$",
            message = "Passport number must be 6-9 uppercase letters/numbers"
    )
    private String passportNo;
}

