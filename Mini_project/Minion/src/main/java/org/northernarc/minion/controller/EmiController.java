package org.northernarc.minion.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.northernarc.minion.dto.EmiPaymentRequest;
import org.northernarc.minion.model.EmiPayment;
import org.northernarc.minion.model.EmiSchedule;
import org.northernarc.minion.repository.EmiScheduleRepository;
import org.northernarc.minion.service.EmiPaymentService;
import org.northernarc.minion.service.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/emis")
@Tag(name = "EMI Management", description = "EMI payment and penalty management endpoints")
public class EmiController {

    private static final Logger logger = LoggerFactory.getLogger(EmiController.class);

    private final EmiScheduleRepository emiScheduleRepository;
    private final LoanService loanService;
    private final EmiPaymentService emiPaymentService;

    public EmiController(EmiScheduleRepository emiScheduleRepository,
                         LoanService loanService,
                         EmiPaymentService emiPaymentService) {
        this.emiScheduleRepository = emiScheduleRepository;
        this.loanService = loanService;
        this.emiPaymentService = emiPaymentService;
    }

    @GetMapping("/reports/highest-overdue")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Get highest overdue EMI", description = "Retrieve the EMI with highest overdue amount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Highest overdue EMI retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin or Manager role required")
    })
    @SecurityRequirement(name = "Bearer JWT")
    public EmiSchedule highestOverdueEmi() {
        logger.info("Fetching highest overdue EMI");
        return emiScheduleRepository.findHighestOverdueEmi();
    }

    @GetMapping("/payments/reports/latest")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Get latest EMI payments", description = "Retrieve the latest EMI payment records")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Latest payments retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin or Manager role required")
    })
    @SecurityRequirement(name = "Bearer JWT")
    public List<EmiPayment> latestPayment() {
        logger.info("Fetching latest EMI payments");
        return loanService.getLatestPayments();
    }

    @PostMapping("/{emiId}/payments")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','USER')")
    @Operation(summary = "Pay EMI", description = "Record an EMI payment for a specific EMI schedule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "EMI payment recorded successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "EMI schedule not found")
    })
    @SecurityRequirement(name = "Bearer JWT")
    public ResponseEntity<EmiSchedule> payEmi(
            @Parameter(description = "EMI Schedule ID") @PathVariable Long emiId,
            @Valid @RequestBody EmiPaymentRequest request) {
        logger.info("Processing EMI payment for EMI ID: {} with amount: {}", emiId, request.amount());
        EmiSchedule updatedEmi = emiPaymentService.payEmi(emiId, request);
        logger.info("EMI payment processed successfully. New status: {}", updatedEmi.getStatus());
        return ResponseEntity.ok(updatedEmi);
    }

    @PatchMapping("/{emiId}/penalty/recalculate")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Recalculate EMI penalty", description = "Recalculate penalty for an overdue EMI")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Penalty recalculated successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin or Manager role required"),
            @ApiResponse(responseCode = "404", description = "EMI schedule not found")
    })
    @SecurityRequirement(name = "Bearer JWT")
    public ResponseEntity<EmiSchedule> recalculatePenalty(
            @Parameter(description = "EMI Schedule ID") @PathVariable Long emiId) {
        logger.info("Recalculating penalty for EMI ID: {}", emiId);
        EmiSchedule updatedEmi = emiPaymentService.recalculatePenalty(emiId);
        logger.info("Penalty recalculated for EMI ID: {}, new penalty: {}", emiId, updatedEmi.getPenaltyAmount());
        return ResponseEntity.ok(updatedEmi);
    }
}

