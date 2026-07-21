package org.northernarc.assessment4.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.northernarc.assessment4.dto.CustomerSummaryDTO;
import org.northernarc.assessment4.dto.DashboardResponse;
import org.northernarc.assessment4.model.Account;
import org.northernarc.assessment4.model.Customer;
import org.northernarc.assessment4.model.Transaction;
import org.northernarc.assessment4.service.BankService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Banking", description = "Banking operations API")
public class BankController {

    private final BankService bankService;

    // --- Core Onboarding & Setup Endpoints ---
    @PostMapping("/customers")
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        return new ResponseEntity<>(bankService.saveCustomer(customer), HttpStatus.CREATED);
    }

    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account account) {
        return new ResponseEntity<>(bankService.saveAccount(account), HttpStatus.CREATED);
    }

    // --- Task 6: Pagination & Sorting (Default: Balance DESC) ---
    @GetMapping("/accounts")
    @Operation(summary = "Get accounts with pagination sorted by balance descending")
    public ResponseEntity<Page<Account>> getAllAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "balance"));
        return ResponseEntity.ok(bankService.getAllAccountsPaginated(pageable));
    }

    // --- Task 9: Role Based Access Control Endpoints ---
    @DeleteMapping("/accounts/{accountNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountNumber) {
        bankService.deleteAccount(accountNumber);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/accounts/{accountNumber}/balance")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> updateAccountBalance(
            @PathVariable String accountNumber,
            @RequestParam double amount) {

        bankService.increaseAccountBalance(accountNumber, amount);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/accounts/view/{accountNumber}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Account>> viewAccountsByType(
            @PathVariable String accountNumber,
            @RequestParam String type) {

        return ResponseEntity.ok(bankService.getAccountsByType(type));
    }

    // --- Task 7: DTO Projection Aggregation ---
    @GetMapping("/customers/{customerId}/summary")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<CustomerSummaryDTO> getCustomerSummary(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(bankService.getCustomerSummary(customerId));
    }

    // --- Task 3 & 4: Query Operations ---
    @GetMapping("/customers/rich")
    public ResponseEntity<List<Customer>> getRichCustomers(
            @RequestParam double threshold) {

        return ResponseEntity.ok(bankService.getRichCustomers(threshold));
    }

    @GetMapping("/branches/balances")
    public ResponseEntity<Map<String, Double>> getBranchBalances() {
        return ResponseEntity.ok(bankService.getTotalBalancePerBranch());
    }

    @GetMapping("/customers/multiple-accounts")
    public ResponseEntity<List<Customer>> getCustomersWithMultipleAccounts() {
        return ResponseEntity.ok(bankService.getCustomersWithMultipleAccounts());
    }

    @GetMapping("/transactions/latest")
    public ResponseEntity<Transaction> getLatestTransaction() {
        return ResponseEntity.ok(bankService.getLatestTransaction());
    }

    @GetMapping("/accounts/idle")
    public ResponseEntity<List<Account>> getIdleAccounts() {
        return ResponseEntity.ok(bankService.getAccountsWithNoTransactions());
    }

    // --- Final Challenge: Metrics Dashboard Generation ---
    @GetMapping("/dashboard")
    @Operation(summary = "Get dashboard aggregate metrics")
    public ResponseEntity<DashboardResponse> getDashboardMetrics() {
        return ResponseEntity.ok(bankService.getDashboardMetrics());
    }
}