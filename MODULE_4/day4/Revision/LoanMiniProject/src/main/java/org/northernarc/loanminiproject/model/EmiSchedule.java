package org.northernarc.loanminiproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projectemischedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmiSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emiId;

    @Positive
    private Integer installmentNumber;

    private LocalDate dueDate;

    @Positive
    private Double amountDue;

    @Positive
    private Double principalComponent;

    @Positive
    private Double interestComponent;

    private double amountPaid;

    private LocalDate paymentDate;

    private String status;

    private Integer daysPastDue;

    private double penaltyAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id")
    private Loan loan;

    @OneToMany(
            mappedBy = "emiSchedule",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<EmiPayment> payments = new ArrayList<>();

    public void addPayment(EmiPayment payment) {
        payments.add(payment);
        payment.setEmiSchedule(this);
    }

    public void removePayment(EmiPayment payment) {
        payments.remove(payment);
        payment.setEmiSchedule(null);
    }
}