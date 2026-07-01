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
import jakarta.persistence.UniqueConstraint;
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
@Table(
        name = "emi_schedules",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_loan_installment", columnNames = {"loan_id", "installment_number"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmiSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emiId;

    @NotNull(message = "Installment number is required")
    @Min(value = 1, message = "Installment number should be at least 1")
    @Column(name = "installment_number", nullable = false)
    private BigInteger installmentNumber;

    @NotNull(message = "Due date is required")
    @Column(nullable = false)
    private LocalDate dueDate;

    @NotNull(message = "Amount due is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Amount due cannot be negative")
    @Digits(integer = 15, fraction = 2, message = "Amount due should be a valid monetary value")
    @Column(nullable = false, precision = 17, scale = 2)
    private BigDecimal amountDue;

    @NotNull(message = "Principal component is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Principal component cannot be negative")
    @Digits(integer = 15, fraction = 2, message = "Principal component should be a valid monetary value")
    @Column(nullable = false, precision = 17, scale = 2)
    private BigDecimal principalComponent;

    @NotNull(message = "Interest component is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Interest component cannot be negative")
    @Digits(integer = 15, fraction = 2, message = "Interest component should be a valid monetary value")
    @Column(nullable = false, precision = 17, scale = 2)
    private BigDecimal interestComponent;

    @DecimalMin(value = "0.00", inclusive = true, message = "Amount paid cannot be negative")
    @Digits(integer = 15, fraction = 2, message = "Amount paid should be a valid monetary value")
    @Column(nullable = false, precision = 17, scale = 2)
    private Double amountPaid = 0.0;

    @Column
    private LocalDate paymentDate;

    @NotBlank(message = "EMI status is required")
    @Column(nullable = false, length = 20)
    private String status;

    @NotNull(message = "Days past due is required")
    @Min(value = 0, message = "Days past due cannot be negative")
    @Column(nullable = false)
    private Integer daysPastDue = 0;

    @DecimalMin(value = "0.00", inclusive = true, message = "Penalty amount cannot be negative")
    @Digits(integer = 15, fraction = 2, message = "Penalty amount should be a valid monetary value")
    @Column(nullable = false, precision = 17, scale = 2)
    private Double penaltyAmount = 0.0;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    @OneToMany(mappedBy = "emiSchedule", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<EmiPayment> emiPayments = new ArrayList<>();

    public void addEmiPayment(EmiPayment emiPayment) {
        emiPayments.add(emiPayment);
        emiPayment.setEmiSchedule(this);
    }

    public void removeEmiPayment(EmiPayment emiPayment) {
        emiPayments.remove(emiPayment);
        emiPayment.setEmiSchedule(null);
    }

    // Backward-compatible aliases for legacy tests.
    public List<EmiPayment> getPayments() {
        return emiPayments;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.toUpperCase();
    }

    public void setStatus(EmiStatus status) {
        this.status = status == null ? null : status.name();
    }

    public void setAmountPaid(int amountPaid) {
        this.amountPaid = (double) amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public void setPenaltyAmount(int penaltyAmount) {
        this.penaltyAmount = (double) penaltyAmount;
    }

    public void setPenaltyAmount(Double penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }
}

