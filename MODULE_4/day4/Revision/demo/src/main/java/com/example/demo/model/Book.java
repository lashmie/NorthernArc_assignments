package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @NotBlank(message = "ISBN cannot be blank")
    private String isbn;

    @NotBlank(message = "Book title cannot be blank")
    private String title;

    @NotBlank(message = "Book type cannot be blank")
    private String bookType;

    @PositiveOrZero(message = "Daily fine rate cannot be negative")
    private Double dailyFineRate;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private List<IssueRecord> issueRecords = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private List<FineTransaction> fineTransactions = new ArrayList<>();

    public List<IssueRecord> getIssueRecords() {
        return issueRecords;
    }

}
