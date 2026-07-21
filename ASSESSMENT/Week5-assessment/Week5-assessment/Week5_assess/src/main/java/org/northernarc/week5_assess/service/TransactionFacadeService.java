package org.northernarc.week5_assess.service;

import java.util.List;

import org.northernarc.week5_assess.dto.TransactionHistoryResponse;
import org.northernarc.week5_assess.dto.TransferRequest;
import org.northernarc.week5_assess.dto.TransferResponse;
import org.northernarc.week5_assess.entity.Transaction;
import org.springframework.stereotype.Service;

@Service
public class TransactionFacadeService implements TransactionService {

    private final TransactionServiceImpl transactionServiceImpl;

    public TransactionFacadeService(TransactionServiceImpl transactionServiceImpl) {
        this.transactionServiceImpl = transactionServiceImpl;
    }

    public TransferResponse transfer(TransferRequest request) {
        transactionServiceImpl.transfer(request.getSourceAccountNumber(), request.getDestinationAccountNumber(), request.getAmount());
        TransferResponse response = new TransferResponse();
        response.setReferenceId("TXN-0001");
        response.setAmount(request.getAmount());
        return response;
    }

    @Override
    public Object transfer(Object request) {
        if (request instanceof TransferRequest transferRequest) {
            return transfer(transferRequest);
        }
        return null;
    }

    @Override
    public List<TransactionHistoryResponse> getTransactionsForAccount(String accountNumber) {
        List<Transaction> txns = transactionServiceImpl.getTransactionsForAccount(accountNumber);
        return txns.stream().map(this::toResponse).toList();
    }

    @Override
    public List<?> getAllTransactions() {
        return transactionServiceImpl.getAllTransactions();
    }

    @Override
    public Object getTransactionById(Long id) {
        return transactionServiceImpl.getTransactionById(id);
    }

    @Override
    public List<?> getTransactionsByAccountId(Long accountId) {
        return transactionServiceImpl.getTransactionsByAccountId(accountId);
    }

    private TransactionHistoryResponse toResponse(Transaction transaction) {
        TransactionHistoryResponse response = new TransactionHistoryResponse();
        response.setReferenceId(transaction.getReferenceId());
        response.setAmount(transaction.getAmount());
        response.setType(transaction.getType());
        response.setCreatedAt(transaction.getCreatedAt());
        return response;
    }
}

