package org.northernarc.loanminiproject.controller;

import org.northernarc.loanminiproject.dto.LoanDashboardDto;
import org.northernarc.loanminiproject.dto.LoanSummaryDto;
import org.northernarc.loanminiproject.model.Customer;
import org.northernarc.loanminiproject.model.Loan;
import org.northernarc.loanminiproject.model.LoanStatus;
import org.northernarc.loanminiproject.repository.CustomerRepository;
import org.northernarc.loanminiproject.repository.LoanRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.northernarc.loanminiproject.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    public static class LoanCreateRequest {
        @NotBlank
        private String loanType;

        @NotNull
        @Positive
        private Double principalAmount;

        @NotNull
        @Positive
        private Double annualInterestRate;

        @NotNull
        @Positive
        private Integer tenureMonths;

        @NotNull
        private Long customerId;

        public String getLoanType() {
            return loanType;
        }

        public Double getPrincipalAmount() {
            return principalAmount;
        }

        public Double getAnnualInterestRate() {
            return annualInterestRate;
        }

        public Integer getTenureMonths() {
            return tenureMonths;
        }

        public Long getCustomerId() {
            return customerId;
        }
    }

    @Autowired
    private LoanService loanService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LoanRepository loanRepository;

    @PostMapping
    public ResponseEntity<Loan> createLoan(@Valid @RequestBody LoanCreateRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        Loan loan = new Loan();
        loan.setLoanType(request.getLoanType());
        loan.setPrincipalAmount(request.getPrincipalAmount());
        loan.setAnnualInterestRate(request.getAnnualInterestRate());
        loan.setTenureMonths(request.getTenureMonths());
        loan.setDisbursementDate(java.time.LocalDate.now());
        loan.setLoanStatus(LoanStatus.ACTIVE.name());

        double monthlyRate = request.getAnnualInterestRate() / (12 * 100);
        int months = request.getTenureMonths();
        double emi = (request.getPrincipalAmount() * monthlyRate * Math.pow(1 + monthlyRate, months))
                / (Math.pow(1 + monthlyRate, months) - 1);
        loan.setEmiAmount(emi);

        loan.setCustomer(customer);

        Loan savedLoan = loanRepository.save(loan);
        return new ResponseEntity<>(savedLoan, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<Loan>> getAllLoans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Loan> loans = loanService.getAllLoansOrderedByPrincipalDesc(page, size);
        return new ResponseEntity<>(loans, HttpStatus.OK);
    }

    @GetMapping("/summaries")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Page<LoanSummaryDto>> getAllLoanSummaries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<LoanSummaryDto> summaries = loanService.getAllLoanSummaries(page, size);
        return new ResponseEntity<>(summaries, HttpStatus.OK);
    }

    @GetMapping("/summaries/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<LoanSummaryDto>> getLoanSummariesByStatus(
            @RequestParam LoanStatus status) {
        List<LoanSummaryDto> summaries = loanService.getLoanSummariesByStatus(status);
        return new ResponseEntity<>(summaries, HttpStatus.OK);
    }

    @GetMapping("/summaries/type")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<LoanSummaryDto>> getLoanSummariesByType(
            @RequestParam String loanType) {
        List<LoanSummaryDto> summaries = loanService.getLoanSummariesByType(loanType);
        return new ResponseEntity<>(summaries, HttpStatus.OK);
    }

    @GetMapping("/status")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<Loan>> getLoansByStatus(
            @RequestParam LoanStatus status) {
        List<Loan> loans = loanService.findLoansByStatus(status);
        return new ResponseEntity<>(loans, HttpStatus.OK);
    }

    @GetMapping("/type")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<Loan>> getLoansByType(
            @RequestParam String loanType) {
        List<Loan> loans = loanService.findLoansByType(loanType);
        return new ResponseEntity<>(loans, HttpStatus.OK);
    }

    @GetMapping("/city")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<Loan>> getLoansByCity(
            @RequestParam String city) {
        List<Loan> loans = loanService.findLoansByCity(city);
        return new ResponseEntity<>(loans, HttpStatus.OK);
    }

    @GetMapping("/zero-overdue")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<Loan>> getLoansWithZeroOverdueEmis() {
        List<Loan> loans = loanService.findLoansWithZeroOverdueEmis();
        return new ResponseEntity<>(loans, HttpStatus.OK);
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LoanDashboardDto> getLoanDashboard() {
        LoanDashboardDto dashboard = loanService.getLoanDashboard();
        return new ResponseEntity<>(dashboard, HttpStatus.OK);
    }

    @PutMapping("/interest-rate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateInterestRate(
            @RequestParam String loanType,
            @RequestParam Double newRate) {
        int updated = loanService.reviseInterestRate(loanType, newRate);
        return ResponseEntity.ok(updated + " loan(s) updated successfully.");
    }
}





