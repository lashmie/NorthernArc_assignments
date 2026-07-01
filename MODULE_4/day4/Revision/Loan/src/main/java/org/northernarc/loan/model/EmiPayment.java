package org.northernarc.loan.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "emi_payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmiPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @NotNull(message = "Payment amount is required")
    @DecimalMin(value = "0.01", message = "Payment amount must be greater than 0")
    @Digits(integer = 15, fraction = 2, message = "Payment amount should be a valid monetary value")
    @Column(nullable = false, precision = 17, scale = 2)
    private Double amount;

    @NotBlank(message = "Payment mode is required")
    @Size(max = 30, message = "Payment mode cannot exceed 30 characters")
    @Column(nullable = false, length = 30)
    private String paymentMode;

    @NotNull(message = "Payment date is required")
    @Column(nullable = false)
    private LocalDate paymentDate;

    @NotBlank(message = "Reference number is required")
    @Size(max = 100, message = "Reference number cannot exceed 100 characters")
    @Column(nullable = false, unique = true, length = 100)
    private String referenceNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emi_id", nullable = false)
    private EmiSchedule emiSchedule;
}

