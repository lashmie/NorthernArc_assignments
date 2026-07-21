package org.northernarc.week5_assess.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.northernarc.week5_assess.entity.Account;
import org.northernarc.week5_assess.entity.Customer;
import org.northernarc.week5_assess.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("TransactionRepository - DataJpa Tests")
public class TransactionRepositoryTest {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Test
	@DisplayName("should return transactions in descending order for account")
	void findByAccountNumberOrderByCreatedAtDesc_shouldReturnOrderedTransactions() {
		Customer customer = customerRepository.save(customer());
		Account account = accountRepository.save(account(customer, "SB-1001", "100.00"));

		transactionRepository.save(transaction(account, "TXN-1", "10.00", Instant.now().minusSeconds(120)));
		transactionRepository.save(transaction(account, "TXN-2", "15.00", Instant.now()));

		List<Transaction> actual = transactionRepository.findByAccountNumberOrderByCreatedAtDesc("SB-1001");

		assertEquals(2, actual.size());
		assertEquals("TXN-2", actual.get(0).getReferenceId());
	}

	private static Customer customer() {
		Customer customer = new Customer();
		customer.setName("Asha Raman");
		customer.setEmail("asha@northernarc.org");
		customer.setPhoneNumber("9000000001");
		customer.setActive(true);
		return customer;
	}

	private static Account account(Customer customer, String number, String balance) {
		Account account = new Account();
		account.setCustomer(customer);
		account.setAccountNumber(number);
		account.setBalance(new BigDecimal(balance));
		account.setActive(true);
		return account;
	}

	private static Transaction transaction(Account account, String referenceId, String amount, Instant createdAt) {
		Transaction transaction = new Transaction();
		transaction.setAccount(account);
		transaction.setReferenceId(referenceId);
		transaction.setAmount(new BigDecimal(amount));
		transaction.setCreatedAt(createdAt);
		transaction.setType("DEPOSIT");
		return transaction;
	}
}
