package org.northernarc.ecom.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.northernarc.ecom.model.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO {

    @JsonProperty("customer_id")
    private Integer id;

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Email is required")
    private String email;

    @NotBlank(message = "Address is required")
    private String address;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private Role role;
}