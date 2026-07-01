package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardDTO {
    private Long totalMembers;
    private Long totalBooks;
    private Double totalFinesCollected;
    private String topBranch;
    private String highestFinePayingMember;
}

