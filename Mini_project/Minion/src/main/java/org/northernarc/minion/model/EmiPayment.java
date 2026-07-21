package org.northernarc.minion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "projectemipayment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmiPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Positive
    private Double amount;

    @NotBlank
    private String paymentMode;

    private LocalDate paymentDate;

    @Column(unique = true)
    private String referenceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emi_id")
    private EmiSchedule emiSchedule;
}