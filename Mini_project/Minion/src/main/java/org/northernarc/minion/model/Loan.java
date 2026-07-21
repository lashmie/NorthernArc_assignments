package org.northernarc.minion.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projectloan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    @NotBlank
    private String loanType;

    @Positive
    private Double principalAmount;

    @Positive
    private Double annualInterestRate;

    @Positive
    private Integer tenureMonths;

    private Double emiAmount;

    private LocalDate disbursementDate;

    private String loanStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(
            mappedBy = "loan",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<EmiSchedule> emiSchedules = new ArrayList<>();

    public void addEmiSchedule(EmiSchedule emi) {
        emiSchedules.add(emi);
        emi.setLoan(this);
    }

    public void removeEmiSchedule(EmiSchedule emi) {
        emiSchedules.remove(emi);
        emi.setLoan(null);
    }
}