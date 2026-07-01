package org.northernarc.loan.controller;

import jakarta.validation.Valid;
import org.northernarc.loan.dto.EmiPaymentRequest;
import org.northernarc.loan.dto.LoanApprovalRequest;
import org.northernarc.loan.dto.LoanDashboardDTO;
import org.northernarc.loan.dto.LoanSummaryDTO;
import org.northernarc.loan.exception.BusinessRuleException;
import org.northernarc.loan.exception.NotFoundException;
import org.northernarc.loan.model.Loan;
import org.northernarc.loan.repository.LoanRepository;
import org.northernarc.loan.service.LoanService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/loans", "/api/loans"})
public class LoanController {

    private final LoanService loanService;
    private final LoanRepository loanRepository;

    public LoanController(LoanService loanService, LoanRepository loanRepository) {
        this.loanService = loanService;
        this.loanRepository = loanRepository;
    }

    @PostMapping("/approve")
    public ResponseEntity<LoanSummaryDTO> approveLoan(@Valid @RequestBody LoanApprovalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loanService.approveLoan(request));
    }

    @PostMapping("/emi/pay")
    public ResponseEntity<LoanSummaryDTO> payEmi(@Valid @RequestBody EmiPaymentRequest request) {
        return ResponseEntity.ok(loanService.payEmi(request));
    }

    @GetMapping
    public ResponseEntity<?> getLoans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<LoanSummaryDTO> loans = loanService.getLoans(page, size);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/{loanId}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new NotFoundException("Loan not found"));
        return ResponseEntity.ok(loan);
    }

    @DeleteMapping("/{loanId}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new NotFoundException("Loan not found"));
        loanRepository.delete(loan);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{loanId}/interest")
    public ResponseEntity<Loan> updateInterestRate(@PathVariable Long loanId, @RequestParam("rate") Double rate) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new NotFoundException("Loan not found"));
        if (rate < 0) {
            throw new BusinessRuleException("Rate cannot be negative");
        }
        loan.setAnnualInterestRate(rate);
        return ResponseEntity.ok(loanRepository.save(loan));
    }

    @PostMapping
    public ResponseEntity<Loan> createLoan(@Valid @RequestBody Loan loan) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loanRepository.save(loan));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<LoanDashboardDTO> getDashboard() {
        return ResponseEntity.ok(loanService.getDashboard());
    }

    @PatchMapping("/interest-rate")
    public ResponseEntity<String> reviseInterestRate(
            @RequestParam String loanType,
            @RequestParam Double newRate
    ) {
        int updated = loanService.reviseInterestRateByLoanType(loanType, newRate);
        return ResponseEntity.ok("Updated loans: " + updated);
    }

    @GetMapping("/search/by-type")
    public ResponseEntity<List<LoanSummaryDTO>> getByLoanType(@RequestParam String loanType) {
        return ResponseEntity.ok(loanService.getLoansByLoanType(loanType));
    }

    @GetMapping("/search/by-city")
    public ResponseEntity<List<LoanSummaryDTO>> getByCity(@RequestParam String city) {
        return ResponseEntity.ok(loanService.getLoansByCity(city));
    }

    @GetMapping("/search/by-status")
    public ResponseEntity<List<LoanSummaryDTO>> getByStatus(@RequestParam String status) {
        return ResponseEntity.ok(loanService.getLoansByStatus(status));
    }

    @GetMapping("/search/by-principal")
    public ResponseEntity<List<LoanSummaryDTO>> getByPrincipal(@RequestParam Double amount) {
        return ResponseEntity.ok(loanService.getLoansByPrincipalAmountGreaterThan(amount));
    }
}
