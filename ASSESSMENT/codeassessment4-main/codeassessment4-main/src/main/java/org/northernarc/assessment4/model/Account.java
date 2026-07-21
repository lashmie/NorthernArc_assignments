package org.northernarc.assessment4.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "test_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @NotBlank(message = "Account type is required")
    private String accountType;

    @NotNull(message = "Balance is required")
    @PositiveOrZero(message = "Balance cannot be negative")
    private Double balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonBackReference("customer-accounts")
    private Customer customer;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference("account-transactions")
    private List<Transaction> transactions = new ArrayList<>();
}