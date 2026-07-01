package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberSummaryDTO {
    private String memberName;
    private String membershipBranch;
    private Long numberOfBorrowedBooks;
    private Double totalFinesPaid;
}

