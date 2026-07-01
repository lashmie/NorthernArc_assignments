package org.northernarc.loan.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @NotBlank(message = "Customer name is required")
    @Size(max = 100, message = "Customer name cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String customerName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 150, message = "Email cannot exceed 150 characters")
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Column(nullable = false, length = 100)
    private String password;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must contain 10 to 15 digits")
    @Column(nullable = false, unique = true, length = 15)
    private String phoneNumber;

    @NotBlank(message = "City is required")
    @Size(max = 80, message = "City cannot exceed 80 characters")
    @Column(nullable = false, length = 80)
    private String city;

    @NotNull(message = "Credit score is required")
    @Min(value = 300, message = "Credit score must be at least 300")
    @Max(value = 900, message = "Credit score cannot exceed 900")
    @Column(nullable = false)
    private BigInteger creditScore;

    @NotBlank(message = "Role is required")
    @Column(nullable = false, length = 20)
    private String role = "USER";

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Loan> loans = new ArrayList<>();

    public void addLoan(Loan loan) {
        loans.add(loan);
        loan.setCustomer(this);
    }

    public void removeLoan(Loan loan) {
        loans.remove(loan);
        loan.setCustomer(null);
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = BigInteger.valueOf(creditScore);
    }
}

