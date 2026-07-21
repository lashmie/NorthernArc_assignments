package org.northernarc.week5_assess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.northernarc.week5_assess.entity.Account;
import org.northernarc.week5_assess.entity.Customer;
import org.northernarc.week5_assess.entity.Transaction;
import org.northernarc.week5_assess.exception.AccountNotFoundException;
import org.northernarc.week5_assess.exception.CustomerNotFoundException;
import org.northernarc.week5_assess.exception.DuplicateAccountNumberException;
import org.northernarc.week5_assess.exception.InsufficientBalanceException;
import org.northernarc.week5_assess.repository.AccountRepository;
import org.northernarc.week5_assess.repository.CustomerRepository;
import org.northernarc.week5_assess.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("AccountServiceTest")
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Nested
    @DisplayName("Account Creation")
    class AccountCreationTests {

        @Test
        @DisplayName("should create account successfully")
        void shouldCreateAccountSuccessfully() {
            // Arrange
            Customer customer = customer(1L);
            Account request = accountWithoutId(customer, "ACC1001", "SAVINGS", "0.00");
            Account saved = accountWithId(10L, customer, "ACC1001", "SAVINGS", "0.00");
            when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
            when(accountRepository.existsByAccountNumber(request.getAccountNumber())).thenReturn(false);
            when(accountRepository.save(any(Account.class))).thenReturn(saved);

            // Act
            Account actual = accountService.createAccount(request);

            // Assert
            assertThat(actual.getId()).isEqualTo(10L);
            assertThat(actual.getAccountType()).isEqualTo("SAVINGS");
            verify(customerRepository).findById(customer.getId());
            verify(accountRepository).existsByAccountNumber("ACC1001");
            verify(accountRepository).save(any(Account.class));
            verifyNoMoreInteractions(customerRepository, accountRepository);
        }

        @Test
        @DisplayName("should fail when customer does not exist")
        void shouldFailWhenCustomerDoesNotExist() {
            // Arrange
            Account request = accountWithoutId(customer(1L), "ACC1001", "SAVINGS", "0.00");
            when(customerRepository.findById(1L)).thenReturn(Optional.empty());

            // Act + Assert
            assertThatThrownBy(() -> accountService.createAccount(request))
                .isInstanceOf(CustomerNotFoundException.class);
            verify(accountRepository, never()).save(any(Account.class));
        }

        @Test
        @DisplayName("should allow opening balance zero")
        void shouldAllowOpeningBalanceZero() {
            // Arrange
            Customer customer = customer(1L);
            Account request = accountWithoutId(customer, "ACC1001", "CURRENT", "0.00");
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
            when(accountRepository.existsByAccountNumber("ACC1001")).thenReturn(false);
            when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

            // Act
            Account actual = accountService.createAccount(request);

            // Assert
            assertThat(actual.getBalance()).isEqualByComparingTo("0.00");
        }

        @Test
        @DisplayName("should allow positive opening balance")
        void shouldAllowPositiveOpeningBalance() {
            // Arrange
            Customer customer = customer(1L);
            Account request = accountWithoutId(customer, "ACC1001", "CURRENT", "100.25");
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
            when(accountRepository.existsByAccountNumber("ACC1001")).thenReturn(false);
            when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

            // Act
            Account actual = accountService.createAccount(request);

            // Assert
            assertThat(actual.getBalance()).isEqualByComparingTo("100.25");
        }

        @Test
        @DisplayName("should reject negative opening balance")
        void shouldRejectNegativeOpeningBalance() {
            // Arrange
            Account request = accountWithoutId(customer(1L), "ACC1001", "SAVINGS", "-1.00");

            // Act + Assert
            assertThatThrownBy(() -> accountService.createAccount(request))
                .isInstanceOf(IllegalArgumentException.class);
            verify(accountRepository, never()).save(any(Account.class));
        }

        @Test
        @DisplayName("should throw duplicate account number exception")
        void shouldThrowDuplicateAccountNumberException() {
            // Arrange
            Customer customer = customer(1L);
            Account request = accountWithoutId(customer, "ACC1001", "SAVINGS", "10.00");
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
            when(accountRepository.existsByAccountNumber("ACC1001")).thenReturn(true);

            // Act + Assert
            assertThatThrownBy(() -> accountService.createAccount(request))
                .isInstanceOf(DuplicateAccountNumberException.class);
            verify(accountRepository, never()).save(any(Account.class));
        }

        @Test
        @DisplayName("should reject null account number")
        void shouldRejectNullAccountNumber() {
            Account request = accountWithoutId(customer(1L), null, "SAVINGS", "10.00");
            assertThatThrownBy(() -> accountService.createAccount(request)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("should reject blank account number")
        void shouldRejectBlankAccountNumber() {
            Account request = accountWithoutId(customer(1L), "  ", "SAVINGS", "10.00");
            assertThatThrownBy(() -> accountService.createAccount(request)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("should accept SAVINGS account type")
        void shouldAcceptSavingsType() {
            Customer customer = customer(1L);
            Account request = accountWithoutId(customer, "ACC1001", "SAVINGS", "10.00");
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
            when(accountRepository.existsByAccountNumber("ACC1001")).thenReturn(false);
            when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

            Account actual = accountService.createAccount(request);

            assertThat(actual.getAccountType()).isEqualTo("SAVINGS");
        }

        @Test
        @DisplayName("should accept CURRENT account type")
        void shouldAcceptCurrentType() {
            Customer customer = customer(1L);
            Account request = accountWithoutId(customer, "ACC1001", "CURRENT", "10.00");
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
            when(accountRepository.existsByAccountNumber("ACC1001")).thenReturn(false);
            when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

            Account actual = accountService.createAccount(request);

            assertThat(actual.getAccountType()).isEqualTo("CURRENT");
        }

        @Test
        @DisplayName("should reject invalid account type")
        void shouldRejectInvalidAccountType() {
            Account request = accountWithoutId(customer(1L), "ACC1001", "FIXED", "10.00");
            assertThatThrownBy(() -> accountService.createAccount(request)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("should reject null account type")
        void shouldRejectNullAccountType() {
            Account request = accountWithoutId(customer(1L), "ACC1001", null, "10.00");
            assertThatThrownBy(() -> accountService.createAccount(request)).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Deposit")
    class DepositTests {

        @Test
        @DisplayName("should deposit successfully")
        void shouldDepositSuccessfully() {
            // Arrange
            Account existing = accountWithId(1L, customer(1L), "ACC1001", "SAVINGS", "100.00");
            when(accountRepository.findById(1L)).thenReturn(Optional.of(existing));
            when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

            // Act
            Account updated = accountService.deposit(1L, new BigDecimal("25.00"));

            // Assert
            assertThat(updated.getBalance()).isEqualByComparingTo("125.00");
            verify(accountRepository).findById(1L);
            verify(accountRepository).save(existing);
            verify(transactionRepository).save(any(Transaction.class));
        }

        @Test
        @DisplayName("should deposit decimal amount")
        void shouldDepositDecimalAmount() {
            Account existing = accountWithId(1L, customer(1L), "ACC1001", "SAVINGS", "100.10");
            when(accountRepository.findById(1L)).thenReturn(Optional.of(existing));
            when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

            Account updated = accountService.deposit(1L, new BigDecimal("0.25"));

            assertThat(updated.getBalance()).isEqualByComparingTo("100.35");
        }

        @Test
        @DisplayName("should reject deposit amount zero")
        void shouldRejectDepositAmountZero() {
            assertThatThrownBy(() -> accountService.deposit(1L, BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class);
            verifyNoMoreInteractions(accountRepository, transactionRepository);
        }

        @Test
        @DisplayName("should reject deposit negative amount")
        void shouldRejectDepositNegativeAmount() {
            assertThatThrownBy(() -> accountService.deposit(1L, new BigDecimal("-1.00")))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("should throw account not found for deposit")
        void shouldThrowAccountNotFoundForDeposit() {
            when(accountRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> accountService.deposit(99L, new BigDecimal("10.00")))
                .isInstanceOf(AccountNotFoundException.class);

            verify(transactionRepository, never()).save(any(Transaction.class));
        }

        @Test
        @DisplayName("should create transaction record on deposit")
        void shouldCreateTransactionRecordOnDeposit() {
            Account existing = accountWithId(1L, customer(1L), "ACC1001", "SAVINGS", "100.00");
            when(accountRepository.findById(1L)).thenReturn(Optional.of(existing));
            when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

            accountService.deposit(1L, new BigDecimal("10.00"));

            verify(transactionRepository, times(1)).save(any(Transaction.class));
        }

        @Test
        @DisplayName("should not save account on invalid deposit")
        void shouldNotSaveAccountOnInvalidDeposit() {
            assertThatThrownBy(() -> accountService.deposit(1L, BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class);

            verify(accountRepository, never()).save(any(Account.class));
        }
    }

    @Nested
    @DisplayName("Withdrawal")
    class WithdrawalTests {

        @Test
        @DisplayName("should withdraw successfully")
        void shouldWithdrawSuccessfully() {
            Account existing = accountWithId(1L, customer(1L), "ACC1001", "SAVINGS", "100.00");
            when(accountRepository.findById(1L)).thenReturn(Optional.of(existing));
            when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

            Account updated = accountService.withdraw(1L, new BigDecimal("20.00"));

            assertThat(updated.getBalance()).isEqualByComparingTo("80.00");
            verify(transactionRepository).save(any(Transaction.class));
        }

        @Test
        @DisplayName("should withdraw exact available balance")
        void shouldWithdrawExactAvailableBalance() {
            Account existing = accountWithId(1L, customer(1L), "ACC1001", "SAVINGS", "100.00");
            when(accountRepository.findById(1L)).thenReturn(Optional.of(existing));
            when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

            Account updated = accountService.withdraw(1L, new BigDecimal("100.00"));

            assertThat(updated.getBalance()).isEqualByComparingTo("0.00");
        }

        @Test
        @DisplayName("should throw insufficient balance when amount exceeds")
        void shouldThrowInsufficientBalance() {
            Account existing = accountWithId(1L, customer(1L), "ACC1001", "SAVINGS", "50.00");
            when(accountRepository.findById(1L)).thenReturn(Optional.of(existing));

            assertThatThrownBy(() -> accountService.withdraw(1L, new BigDecimal("60.00")))
                .isInstanceOf(InsufficientBalanceException.class);

            assertThat(existing.getBalance()).isEqualByComparingTo("50.00");
            verify(accountRepository, never()).save(any(Account.class));
            verify(transactionRepository, never()).save(any(Transaction.class));
        }

        @Test
        @DisplayName("should reject withdrawal zero")
        void shouldRejectWithdrawalZero() {
            assertThatThrownBy(() -> accountService.withdraw(1L, BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("should reject withdrawal negative")
        void shouldRejectWithdrawalNegative() {
            assertThatThrownBy(() -> accountService.withdraw(1L, new BigDecimal("-1.00")))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("should throw account not found on withdrawal")
        void shouldThrowAccountNotFoundOnWithdrawal() {
            when(accountRepository.findById(101L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> accountService.withdraw(101L, new BigDecimal("10.00")))
                .isInstanceOf(AccountNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Transfer")
    class TransferTests {

        @Test
        @DisplayName("should transfer successfully")
        void shouldTransferSuccessfully() {
            Account source = accountWithId(1L, customer(1L), "ACC1001", "SAVINGS", "100.00");
            Account destination = accountWithId(2L, customer(2L), "ACC1002", "CURRENT", "10.00");

            when(accountRepository.findById(1L)).thenReturn(Optional.of(source));
            when(accountRepository.findById(2L)).thenReturn(Optional.of(destination));
            when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

            accountService.transfer(1L, 2L, new BigDecimal("30.00"));

            assertThat(source.getBalance()).isEqualByComparingTo("70.00");
            assertThat(destination.getBalance()).isEqualByComparingTo("40.00");
            verify(transactionRepository, times(2)).save(any(Transaction.class));
        }

        @Test
        @DisplayName("should fail when source account not found")
        void shouldFailWhenSourceNotFound() {
            when(accountRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> accountService.transfer(1L, 2L, new BigDecimal("10.00")))
                .isInstanceOf(AccountNotFoundException.class);

            verify(accountRepository, never()).save(any(Account.class));
            verify(transactionRepository, never()).save(any(Transaction.class));
        }

        @Test
        @DisplayName("should fail when destination account not found")
        void shouldFailWhenDestinationNotFound() {
            Account source = accountWithId(1L, customer(1L), "ACC1001", "SAVINGS", "100.00");
            when(accountRepository.findById(1L)).thenReturn(Optional.of(source));
            when(accountRepository.findById(2L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> accountService.transfer(1L, 2L, new BigDecimal("10.00")))
                .isInstanceOf(AccountNotFoundException.class);

            assertThat(source.getBalance()).isEqualByComparingTo("100.00");
        }

        @Test
        @DisplayName("should fail when source and destination are same")
        void shouldFailWhenSourceDestinationSame() {
            assertThatThrownBy(() -> accountService.transfer(1L, 1L, new BigDecimal("10.00")))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("should fail transfer for zero amount")
        void shouldFailTransferForZeroAmount() {
            assertThatThrownBy(() -> accountService.transfer(1L, 2L, BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("should fail transfer for negative amount")
        void shouldFailTransferForNegativeAmount() {
            assertThatThrownBy(() -> accountService.transfer(1L, 2L, new BigDecimal("-1.00")))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("should fail transfer when amount exceeds source balance")
        void shouldFailTransferForInsufficientBalance() {
            Account source = accountWithId(1L, customer(1L), "ACC1001", "SAVINGS", "20.00");
            Account destination = accountWithId(2L, customer(2L), "ACC1002", "CURRENT", "10.00");
            when(accountRepository.findById(1L)).thenReturn(Optional.of(source));
            when(accountRepository.findById(2L)).thenReturn(Optional.of(destination));

            assertThatThrownBy(() -> accountService.transfer(1L, 2L, new BigDecimal("30.00")))
                .isInstanceOf(InsufficientBalanceException.class);

            assertThat(source.getBalance()).isEqualByComparingTo("20.00");
            assertThat(destination.getBalance()).isEqualByComparingTo("10.00");
            verify(accountRepository, never()).save(any(Account.class));
            verify(transactionRepository, never()).save(any(Transaction.class));
        }

        @Test
        @DisplayName("should create two transaction records for transfer")
        void shouldCreateTwoTransactionRecordsForTransfer() {
            Account source = accountWithId(1L, customer(1L), "ACC1001", "SAVINGS", "100.00");
            Account destination = accountWithId(2L, customer(2L), "ACC1002", "CURRENT", "10.00");

            when(accountRepository.findById(1L)).thenReturn(Optional.of(source));
            when(accountRepository.findById(2L)).thenReturn(Optional.of(destination));
            when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

            accountService.transfer(1L, 2L, new BigDecimal("10.00"));

            verify(transactionRepository, times(2)).save(any(Transaction.class));
        }

        @Test
        @DisplayName("should rollback when destination save fails")
        void shouldRollbackWhenDestinationSaveFails() {
            Account source = accountWithId(1L, customer(1L), "ACC1001", "SAVINGS", "100.00");
            Account destination = accountWithId(2L, customer(2L), "ACC1002", "CURRENT", "10.00");

            when(accountRepository.findById(1L)).thenReturn(Optional.of(source));
            when(accountRepository.findById(2L)).thenReturn(Optional.of(destination));
            when(accountRepository.save(source)).thenReturn(source);
            when(accountRepository.save(destination)).thenThrow(new RuntimeException("destination save failed"));

            assertThatThrownBy(() -> accountService.transfer(1L, 2L, new BigDecimal("10.00")))
                .isInstanceOf(RuntimeException.class);

            verify(transactionRepository, never()).save(any(Transaction.class));
        }

        @Test
        @DisplayName("should rollback when transaction save fails")
        void shouldRollbackWhenTransactionSaveFails() {
            Account source = accountWithId(1L, customer(1L), "ACC1001", "SAVINGS", "100.00");
            Account destination = accountWithId(2L, customer(2L), "ACC1002", "CURRENT", "10.00");

            when(accountRepository.findById(1L)).thenReturn(Optional.of(source));
            when(accountRepository.findById(2L)).thenReturn(Optional.of(destination));
            when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));
            when(transactionRepository.save(any(Transaction.class))).thenThrow(new RuntimeException("transaction save failed"));

            assertThatThrownBy(() -> accountService.transfer(1L, 2L, new BigDecimal("10.00")))
                .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("should verify repository interactions for transfer")
        void shouldVerifyRepositoryInteractionsForTransfer() {
            Account source = accountWithId(1L, customer(1L), "ACC1001", "SAVINGS", "100.00");
            Account destination = accountWithId(2L, customer(2L), "ACC1002", "CURRENT", "10.00");

            when(accountRepository.findById(1L)).thenReturn(Optional.of(source));
            when(accountRepository.findById(2L)).thenReturn(Optional.of(destination));
            when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

            accountService.transfer(1L, 2L, new BigDecimal("10.00"));

            verify(accountRepository).findById(1L);
            verify(accountRepository).findById(2L);
            verify(accountRepository, times(2)).save(any(Account.class));
            verify(transactionRepository, times(2)).save(any(Transaction.class));
        }
    }

    private static Customer customer(Long id) {
        Customer c = new Customer();
        c.setId(id);
        c.setName("Customer-" + id);
        return c;
    }

    private static Account accountWithoutId(Customer customer, String number, String type, String balance) {
        Account a = new Account();
        a.setCustomer(customer);
        a.setAccountNumber(number);
        a.setAccountType(type);
        a.setBalance(new BigDecimal(balance));
        return a;
    }

    private static Account accountWithId(Long id, Customer customer, String number, String type, String balance) {
        Account a = accountWithoutId(customer, number, type, balance);
        a.setId(id);
        return a;
    }
}
