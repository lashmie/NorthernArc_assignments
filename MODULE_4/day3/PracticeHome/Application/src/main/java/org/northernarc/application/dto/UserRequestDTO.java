package org.northernarc.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;



@Data
public class UserRequestDTO {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    private String role;
}