package org.northernarc.week5_assess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.northernarc.week5_assess.entity.Account;
import org.northernarc.week5_assess.entity.Transaction;
import org.northernarc.week5_assess.exception.TransactionNotFoundException;
import org.northernarc.week5_assess.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("TransactionServiceTest")
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Nested
    @DisplayName("Transaction Retrieval")
    class RetrievalTests {

        @Test
        @DisplayName("should get all transactions")
        void shouldGetAllTransactions() {
            // Arrange
            when(transactionRepository.findAll()).thenReturn(List.of(transaction(1L, 1L), transaction(2L, 1L)));

            // Act
            List<Transaction> actual = transactionService.getAllTransactions();

            // Assert
            assertThat(actual).hasSize(2);
            verify(transactionRepository).findAll();
            verifyNoMoreInteractions(transactionRepository);
        }

        @Test
        @DisplayName("should get transaction by id")
        void shouldGetTransactionById() {
            // Arrange
            when(transactionRepository.findById(10L)).thenReturn(Optional.of(transaction(10L, 1L)));

            // Act
            Transaction actual = transactionService.getTransactionById(10L);

            // Assert
            assertThat(actual.getId()).isEqualTo(10L);
            verify(transactionRepository).findById(10L);
            verifyNoMoreInteractions(transactionRepository);
        }

        @Test
        @DisplayName("should throw transaction not found when id is missing")
        void shouldThrowTransactionNotFound() {
            // Arrange
            when(transactionRepository.findById(999L)).thenReturn(Optional.empty());

            // Act + Assert
            assertThatThrownBy(() -> transactionService.getTransactionById(999L))
                .isInstanceOf(TransactionNotFoundException.class);
            verify(transactionRepository).findById(999L);
            verifyNoMoreInteractions(transactionRepository);
        }

        @Test
        @DisplayName("should get transactions by account id")
        void shouldGetTransactionsByAccountId() {
            // Arrange
            when(transactionRepository.findByAccountIdOrderByTransactionDateDesc(1L))
                .thenReturn(List.of(transaction(3L, 1L), transaction(2L, 1L), transaction(1L, 1L)));

            // Act
            List<Transaction> actual = transactionService.getTransactionsByAccountId(1L);

            // Assert
            assertThat(actual).hasSize(3);
            assertThat(actual).extracting(Transaction::getId).containsExactly(3L, 2L, 1L);
            verify(transactionRepository).findByAccountIdOrderByTransactionDateDesc(1L);
            verifyNoMoreInteractions(transactionRepository);
        }

        @Test
        @DisplayName("should return empty list when account has no transactions")
        void shouldReturnEmptyListWhenNoTransactions() {
            // Arrange
            when(transactionRepository.findByAccountIdOrderByTransactionDateDesc(44L)).thenReturn(List.of());

            // Act
            List<Transaction> actual = transactionService.getTransactionsByAccountId(44L);

            // Assert
            assertThat(actual).isEmpty();
            verify(transactionRepository).findByAccountIdOrderByTransactionDateDesc(44L);
            verifyNoMoreInteractions(transactionRepository);
        }

        @Test
        @DisplayName("should return transactions in expected order")
        void shouldReturnTransactionsInExpectedOrder() {
            // Arrange
            Transaction latest = transaction(20L, 8L);
            latest.setTransactionDate(Instant.parse("2026-07-08T10:20:30Z"));
            Transaction older = transaction(10L, 8L);
            older.setTransactionDate(Instant.parse("2026-07-07T10:20:30Z"));
            when(transactionRepository.findByAccountIdOrderByTransactionDateDesc(8L)).thenReturn(List.of(latest, older));

            // Act
            List<Transaction> actual = transactionService.getTransactionsByAccountId(8L);

            // Assert
            assertThat(actual).extracting(Transaction::getId).containsExactly(20L, 10L);
            verify(transactionRepository).findByAccountIdOrderByTransactionDateDesc(8L);
            verifyNoMoreInteractions(transactionRepository);
        }

        @Test
        @DisplayName("should propagate repository exception")
        void shouldPropagateRepositoryException() {
            // Arrange
            when(transactionRepository.findAll()).thenThrow(new RuntimeException("db read failed"));

            // Act + Assert
            assertThatThrownBy(() -> transactionService.getAllTransactions())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("db read failed");
            verify(transactionRepository).findAll();
            verifyNoMoreInteractions(transactionRepository);
        }

        @Test
        @DisplayName("should reject null account id")
        void shouldRejectNullAccountId() {
            // Act + Assert
            assertThatThrownBy(() -> transactionService.getTransactionsByAccountId(null))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("should reject invalid account id")
        void shouldRejectInvalidAccountId() {
            // Act + Assert
            assertThatThrownBy(() -> transactionService.getTransactionsByAccountId(-1L))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    private static Transaction transaction(Long id, Long accountId) {
        Account account = new Account();
        account.setId(accountId);
        account.setAccountNumber("ACC-" + accountId);
        account.setBalance(new BigDecimal("100.00"));

        Transaction tx = new Transaction();
        tx.setId(id);
        tx.setAccount(account);
        tx.setType("DEPOSIT");
        tx.setAmount(new BigDecimal("10.00"));
        tx.setTransactionDate(Instant.now());
        return tx;
    }
}
