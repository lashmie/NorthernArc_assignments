package org.northernarc.week5_assess.controller;

import java.math.BigDecimal;

import org.northernarc.week5_assess.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/accounts", "/api/accounts"})
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Object> createAccount(@RequestBody Object request) {
        return ResponseEntity.status(201).body(accountService.createAccount(request));
    }

    @GetMapping
    public ResponseEntity<Object> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Object> getAccountByNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getAccountByNumber(accountNumber));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAccount(@PathVariable Long id, @RequestBody Object request) {
        return ResponseEntity.ok(accountService.updateAccount(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/deposit")
    public ResponseEntity<Object> deposit(@RequestBody Object request) {
        return ResponseEntity.ok(accountService.deposit(request));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Object> withdraw(@RequestBody Object request) {
        return ResponseEntity.ok(accountService.withdraw(request));
    }

    @PostMapping("/transfer")
    public ResponseEntity<Object> transfer(@RequestBody Object request) {
        return ResponseEntity.ok(accountService.transfer(request));
    }

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<Object> depositByNumber(@PathVariable String accountNumber, @RequestBody Object request) {
        BigDecimal amount = accountService.extractAmount(request);
        return ResponseEntity.ok(accountService.deposit(accountNumber, amount));
    }

    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<Object> withdrawByNumber(@PathVariable String accountNumber, @RequestBody Object request) {
        BigDecimal amount = accountService.extractAmount(request);
        return ResponseEntity.ok(accountService.withdraw(accountNumber, amount));
    }
}
