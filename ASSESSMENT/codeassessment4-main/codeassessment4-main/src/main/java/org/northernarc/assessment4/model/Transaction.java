package org.northernarc.assessment4.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "test_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transactionId;

	@NotNull(message = "Amount is required")
	@Positive(message = "Amount must be greater than zero")
	private Double amount;

	@NotBlank(message = "Transaction type is required")
	private String transactionType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_number", nullable = false)
	@JsonBackReference("account-transactions")
	private Account account;

	@NotNull(message = "Transaction date is required")
	private LocalDate transactionDate;

}
