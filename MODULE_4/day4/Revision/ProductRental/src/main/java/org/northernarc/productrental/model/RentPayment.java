package org.northernarc.productrental.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rent_payments")
public class RentPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Positive(message = "Amount must be greater than zero")
    private Double amount;

    @NotNull(message = "Payment mode is required")
    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;

    @NotNull
    @Column(nullable = false)
    private LocalDate paymentDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_id")
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RentalRecord rentalRecord;




}