package org.northernarc.loanminiproject.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="projectemischedule")
public class EmiSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emiId;

    @NotNull
    @Positive
    private Integer installmentNumber;

    @NotNull
    private LocalDate dueDate;

    @NotNull
    @Positive
    private Double amountDue;

    @NotNull
    @Positive
    private Double principalComponent;

    @NotNull
    @Positive
    private Double interestComponent;

    private Double amountPaid;

    private LocalDate paymentDate;

    @NotNull(message = "Status is required")
    private String status;

    @Min(0)
    private Integer daysPastDue;

    @PositiveOrZero
    private Double penaltyAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    @OneToMany(mappedBy = "emiSchedule",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JsonIgnore
    private List<EmiPayment> payments = new ArrayList<>();

    public void setLoan(Loan loan) {
        this.loan = loan;
        if (loan != null && loan.getEmiSchedules() != null) {
            loan.getEmiSchedules().add(this);
        }
    }

    public void setAmountPaid(Number amount) {
        if (amount != null) {
            this.amountPaid = amount.doubleValue();
        } else {
            this.amountPaid = null;
        }
    }

    public void setPenaltyAmount(Number amount) {
        if (amount != null) {
            this.penaltyAmount = amount.doubleValue();
        } else {
            this.penaltyAmount = null;
        }
    }
}
