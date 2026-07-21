package org.northernarc.week5_assess.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.northernarc.week5_assess.entity.Transaction;
import org.northernarc.week5_assess.repository.AccountRepository;
import org.northernarc.week5_assess.repository.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl {

    public TransactionServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
    }

    public void transfer(String sourceAccountNumber, String destinationAccountNumber, BigDecimal amount) {
    }

    public List<Transaction> getTransactionsForAccount(String accountNumber) {
        return Collections.emptyList();
    }

    public List<Transaction> getAllTransactions() {
        return Collections.emptyList();
    }

    public Transaction getTransactionById(Long id) {
        return null;
    }

    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return Collections.emptyList();
    }
}
