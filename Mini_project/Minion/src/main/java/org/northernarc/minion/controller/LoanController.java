package org.northernarc.minion.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.northernarc.minion.dto.DashboardDTO;
import org.northernarc.minion.dto.LoanCreateRequest;
import org.northernarc.minion.dto.LoanDashboardDTO;
import org.northernarc.minion.dto.LoanSummaryDTO;
import org.northernarc.minion.model.Loan;
import org.northernarc.minion.service.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@Tag(name = "Loan Management", description = "Loan creation, retrieval, and management endpoints")
public class LoanController {

    private static final Logger logger = LoggerFactory.getLogger(LoanController.class);

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Create new loan", description = "Create a new loan for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Loan created successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin or Manager role required"),
            @ApiResponse(responseCode = "400", description = "Invalid loan request")
    })
    @SecurityRequirement(name = "Bearer JWT")
    public ResponseEntity<Loan> createLoan(@Valid @RequestBody LoanCreateRequest request) {
        logger.info("Creating new loan for customer ID: {}", request.customerId());
        Loan loan = loanService.createLoan(request);
        logger.info("Loan created successfully with ID: {} and status: {}", loan.getLoanId(), loan.getLoanStatus());
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','USER')")
    @Operation(summary = "Get loans with pagination", description = "Retrieve paginated list of loans")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loans retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @SecurityRequirement(name = "Bearer JWT")
    public Page<LoanSummaryDTO> getLoans(
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        logger.info("Fetching loans - Page: {}, Size: {}", page, size);
        return loanService.getLoansPage(page, size);
    }

    @PatchMapping("/interest-rate")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Revise interest rate by loan type", description = "Update interest rate for a specific loan type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Interest rate updated successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin or Manager role required")
    })
    @SecurityRequirement(name = "Bearer JWT")
    public ResponseEntity<Integer> reviseInterestRate(
            @Parameter(description = "Loan type") @RequestParam String loanType,
            @Parameter(description = "New interest rate") @RequestParam Double rate) {
        logger.info("Updating interest rate for loan type: {} to: {}", loanType, rate);
        Integer updatedCount = loanService.updateInterestRateByLoanType(loanType, rate);
        logger.info("Interest rate updated for {} loans", updatedCount);
        return ResponseEntity.ok(updatedCount);
    }

    @PutMapping("/{loanId}/interest")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Revise interest for specific loan", description = "Update interest rate for a specific loan ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan interest updated successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin or Manager role required"),
            @ApiResponse(responseCode = "404", description = "Loan not found")
    })
    @SecurityRequirement(name = "Bearer JWT")
    public ResponseEntity<Loan> reviseLoanInterest(
            @Parameter(description = "Loan ID") @PathVariable Long loanId,
            @Parameter(description = "New interest rate") @RequestParam("rate") Double rate) {
        logger.info("Updating interest rate for loan ID: {} to: {}", loanId, rate);
        Loan updatedLoan = loanService.updateLoanInterestById(loanId, rate);
        logger.info("Interest rate updated for loan ID: {}", loanId);
        return ResponseEntity.ok(updatedLoan);
    }

    @DeleteMapping("/{loanId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete loan", description = "Delete a loan by ID (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Loan deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Loan not found")
    })
    @SecurityRequirement(name = "Bearer JWT")
    public ResponseEntity<Void> deleteLoan(@Parameter(description = "Loan ID") @PathVariable Long loanId) {
        logger.info("Deleting loan with ID: {}", loanId);
        loanService.deleteLoan(loanId);
        logger.info("Loan with ID: {} deleted successfully", loanId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reports/collection-by-city")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Get EMI collection by city", description = "Generate report of total EMI collection grouped by city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report generated successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin or Manager role required")
    })
    @SecurityRequirement(name = "Bearer JWT")
    public List<Object[]> totalEmiCollectionByCity() {
        logger.info("Generating EMI collection by city report");
        return loanService.getTotalEmiCollectionByCity();
    }

    @GetMapping("/reports/zero-overdue")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Get loans with zero overdue", description = "Retrieve loans that have no overdue EMIs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loans retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin or Manager role required")
    })
    @SecurityRequirement(name = "Bearer JWT")
    public List<LoanSummaryDTO> loansWithZeroOverdue() {
        logger.info("Fetching loans with zero overdue EMIs");
        return loanService.getLoansWithZeroOverdueEmis();
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Get loan dashboard", description = "Retrieve loan dashboard data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dashboard data retrieved successfully")
    })
    public LoanDashboardDTO dashboard() {
        logger.info("Fetching loan dashboard");
        return loanService.getDashboard();
    }

    @GetMapping("/final-dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get final admin dashboard", description = "Retrieve comprehensive admin dashboard (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dashboard data retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required")
    })
    @SecurityRequirement(name = "Bearer JWT")
    public DashboardDTO finalDashboard() {
        logger.info("Fetching final admin dashboard");
        return loanService.getFinalDashboard();
    }
}

