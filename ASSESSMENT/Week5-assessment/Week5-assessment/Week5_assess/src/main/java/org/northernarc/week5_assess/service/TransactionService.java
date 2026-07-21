package org.northernarc.week5_assess.service;

import java.util.List;

import org.northernarc.week5_assess.dto.TransactionHistoryResponse;
import org.northernarc.week5_assess.dto.TransferRequest;
import org.northernarc.week5_assess.dto.TransferResponse;

public interface TransactionService {
    Object transfer(Object request);

    List<?> getTransactionsForAccount(String accountNumber);

    List<?> getAllTransactions();

    Object getTransactionById(Long id);

    List<?> getTransactionsByAccountId(Long accountId);
}
