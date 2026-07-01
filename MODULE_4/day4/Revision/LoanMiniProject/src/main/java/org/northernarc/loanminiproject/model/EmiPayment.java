package org.northernarc.loanminiproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="projectemipayment")
public class EmiPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @NotNull
    @Positive
    private Double amount;

    @NotBlank(message = "Payment mode is required")
    private String paymentMode;

    @NotNull
    private LocalDate paymentDate;

    @NotBlank(message = "Reference number is required")
    @Column(unique = true)
    private String referenceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emi_id", nullable = false)
    private EmiSchedule emiSchedule;

    public void setEmiSchedule(EmiSchedule emiSchedule) {
        this.emiSchedule = emiSchedule;
        if (emiSchedule != null && emiSchedule.getPayments() != null) {
            emiSchedule.getPayments().add(this);
        }
    }
}
