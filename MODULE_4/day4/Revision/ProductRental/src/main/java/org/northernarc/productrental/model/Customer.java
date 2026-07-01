package org.northernarc.productrental.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @NotBlank(message = "Customer name cannot be empty")
    @Size(min = 3, max = 50)
    private String customerName;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Enter a valid email")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must contain at least 6 characters")
    private String password;

    @NotBlank(message = "City cannot be empty")
    private String city;
    private String role;

    @JsonIgnore
    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<RentalRecord> rentalRecords = new ArrayList<>();

}