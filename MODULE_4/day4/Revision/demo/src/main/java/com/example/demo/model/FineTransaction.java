package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "fine_transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FineTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Positive(message = "Fine amount must be greater than zero")
    private Double amount;

    @NotBlank(message = "Payment type cannot be blank")
    private String paymentType;

    @NotNull(message = "Payment date is required")
    private LocalDate paymentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_isbn")
    @JsonIgnore
    @NotNull(message = "Book is required")
    private Book book;

}