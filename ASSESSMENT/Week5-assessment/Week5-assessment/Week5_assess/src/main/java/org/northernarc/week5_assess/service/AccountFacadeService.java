package org.northernarc.week5_assess.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.northernarc.week5_assess.dto.AccountRequest;
import org.northernarc.week5_assess.dto.AccountResponse;
import org.northernarc.week5_assess.entity.Account;
import org.springframework.stereotype.Service;

@Service
public class AccountFacadeService implements AccountService {

    private final AccountServiceImpl accountServiceImpl;

    public AccountFacadeService(AccountServiceImpl accountServiceImpl) {
        this.accountServiceImpl = accountServiceImpl;
    }

    public AccountResponse createAccount(AccountRequest request) {
        if (request.getAccountType() == null || request.getAccountType().trim().isEmpty()) {
            throw new IllegalArgumentException("Account type is required");
        }
        if (!"SAVINGS".equalsIgnoreCase(request.getAccountType()) && !"CURRENT".equalsIgnoreCase(request.getAccountType())) {
            throw new IllegalArgumentException("Unsupported account type");
        }
        return toResponse(accountServiceImpl.createAccount(request.getCustomerId(), request.getAccountType()));
    }

    @Override
    public Object createAccount(Object request) {
        if (request instanceof AccountRequest accountRequest) {
            return createAccount(accountRequest);
        }
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

    @Override
    public AccountResponse getAccountByNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Account number is required");
        }
        return toResponse(accountServiceImpl.getAccountByNumber(accountNumber));
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

    @Override
    public AccountResponse deposit(String accountNumber, BigDecimal amount) {
        return toResponse(accountServiceImpl.deposit(accountNumber, amount));
    }

    @Override
    public AccountResponse withdraw(String accountNumber, BigDecimal amount) {
        return toResponse(accountServiceImpl.withdraw(accountNumber, amount));
    }

    @Override
    public BigDecimal extractAmount(Object request) {
        return accountServiceImpl.extractAmount(request);
    }

    private AccountResponse toResponse(Account account) {
        AccountResponse response = new AccountResponse();
        response.setAccountNumber(account.getAccountNumber());
        response.setBalance(account.getBalance());
        return response;
    }
}

