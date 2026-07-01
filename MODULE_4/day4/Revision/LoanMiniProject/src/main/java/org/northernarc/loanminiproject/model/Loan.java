package org.northernarc.loanminiproject.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="projectloan")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    @NotBlank(message = "Loan type is required")
    private String loanType;

    @NotNull
    @Positive
    private Double principalAmount;

    @NotNull
    @Positive
    private Double annualInterestRate;

    @NotNull
    @Positive
    private Integer tenureMonths;

    private Double emiAmount;

    @NotNull
    private LocalDate disbursementDate;

    @NotNull(message = "Loan status is required")
    private String loanStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "loan",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JsonIgnore
    private List<EmiSchedule> emiSchedules = new ArrayList<>();

    public void setCustomer(Customer customer) {
        this.customer = customer;
        if (customer != null && customer.getLoans() != null) {
            customer.getLoans().add(this);
        }
    }
}
