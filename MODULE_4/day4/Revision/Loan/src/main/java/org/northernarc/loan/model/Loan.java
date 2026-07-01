package org.northernarc.loan.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    @NotBlank(message = "Loan type is required")
    @Column(nullable = false, length = 50)
    private String loanType;

    @NotNull(message = "Principal amount is required")
    @DecimalMin(value = "0.01", message = "Principal amount must be greater than 0")
    @Digits(integer = 15, fraction = 2, message = "Principal amount should be a valid monetary value")
    @Column(nullable = false, precision = 17, scale = 2)
    private BigDecimal principalAmount;

    @NotNull(message = "Annual interest rate is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Interest rate cannot be negative")
    @Digits(integer = 3, fraction = 2, message = "Interest rate should be a valid percentage")
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal annualInterestRate;

    @NotNull(message = "Tenure in months is required")
    @Min(value = 1, message = "Tenure should be at least 1 month")
    @Column(nullable = false)
    private BigInteger tenureMonths;

    @NotNull(message = "EMI amount is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "EMI amount cannot be negative")
    @Digits(integer = 15, fraction = 2, message = "EMI amount should be a valid monetary value")
    @Column(nullable = false, precision = 17, scale = 2)
    private BigDecimal emiAmount;

    @NotNull(message = "Disbursement date is required")
    @Column(nullable = false)
    private LocalDate disbursementDate;

    @NotBlank(message = "Loan status is required")
    @Column(nullable = false, length = 30)
    private String loanStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<EmiSchedule> emiSchedules = new ArrayList<>();

    public void addEmiSchedule(EmiSchedule emiSchedule) {
        emiSchedules.add(emiSchedule);
        emiSchedule.setLoan(this);
    }

    public void removeEmiSchedule(EmiSchedule emiSchedule) {
        emiSchedules.remove(emiSchedule);
        emiSchedule.setLoan(null);
    }
}

