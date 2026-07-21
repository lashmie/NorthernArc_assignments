package org.northernarc.week5_assess.service;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    Object createAccount(Object request);

    List<Object> getAllAccounts();

    Object getAccountById(Long id);

    Object getAccountByNumber(String accountNumber);

    Object updateAccount(Long id, Object request);

    void deleteAccount(Long id);

    Object deposit(Object request);

    Object withdraw(Object request);

    Object transfer(Object request);

    Object deposit(String accountNumber, BigDecimal amount);

    Object withdraw(String accountNumber, BigDecimal amount);

    BigDecimal extractAmount(Object request);
}
