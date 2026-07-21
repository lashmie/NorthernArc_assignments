package org.northernarc.week5_assess.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.northernarc.week5_assess.entity.Account;
import org.northernarc.week5_assess.repository.AccountRepository;
import org.northernarc.week5_assess.repository.CustomerRepository;
import org.northernarc.week5_assess.repository.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    public AccountServiceImpl(AccountRepository accountRepository, CustomerRepository customerRepository) {
    }

    public AccountServiceImpl(
        AccountRepository accountRepository,
        CustomerRepository customerRepository,
        TransactionRepository transactionRepository
    ) {
    }

    public Account createAccount(Long customerId, String accountType) {
        return null;
    }

    public Account createAccount(Account request) {
        return null;
    }

    @Override
    public Object createAccount(Object request) {
        return null;
    }

    @Override
    public List<Object> getAllAccounts() {
        return Collections.emptyList();
    }

    @Override
    public Object getAccountById(Long id) {
        return null;
    }

    public Account getAccountByNumber(String accountNumber) {
        return null;
    }

    @Override
    public Object updateAccount(Long id, Object request) {
        return null;
    }

    @Override
    public void deleteAccount(Long id) {
    }

    @Override
    public Object deposit(Object request) {
        return null;
    }

    @Override
    public Object withdraw(Object request) {
        return null;
    }

    @Override
    public Object transfer(Object request) {
        return null;
    }

    public Account deposit(String accountNumber, BigDecimal amount) {
        return null;
    }

    public Account deposit(Long accountId, BigDecimal amount) {
        return null;
    }

    public Account withdraw(String accountNumber, BigDecimal amount) {
        return null;
    }

    public Account withdraw(Long accountId, BigDecimal amount) {
        return null;
    }

    public void transfer(Long sourceAccountId, Long destinationAccountId, BigDecimal amount) {
    }

    @Override
    public BigDecimal extractAmount(Object request) {
        return BigDecimal.ZERO;
    }
}
