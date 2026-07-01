package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ApiErrorResponse {
    private String status;
    private String message;
    private LocalDateTime timestamp;
    private List<String> errors;
}

