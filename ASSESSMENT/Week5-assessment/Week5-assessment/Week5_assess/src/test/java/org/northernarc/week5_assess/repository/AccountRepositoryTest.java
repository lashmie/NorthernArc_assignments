package org.northernarc.week5_assess.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.northernarc.week5_assess.entity.Account;
import org.northernarc.week5_assess.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
@DisplayName("AccountRepository - DataJpa Tests")
class AccountRepositoryTest {

	@Autowired
	private AccountRepository repository;

	@Autowired
	private CustomerRepository customerRepository;

	@Nested
	@DisplayName("Create and Read")
	class CreateReadTests {

		@Test
		@DisplayName("save account successfully")
		void saveAccountSuccessfully() {
			// Arrange
			Customer customer = customerRepository.save(newCustomer("Asha Raman", "asha@northernarc.org", "9000000001"));
			Account account = newAccount(customer, "SB-1001", "150.00");

			// Act
			Account saved = repository.save(account);

			// Assert
			assertThat(saved.getId()).isNotNull();
			assertThat(saved.getAccountNumber()).isEqualTo("SB-1001");
			assertThat(saved.getBalance()).isEqualByComparingTo("150.00");
		}

		@Test
		@DisplayName("find account by id")
		void findAccountById() {
			// Arrange
			Customer customer = customerRepository.save(newCustomer("Asha Raman", "asha@northernarc.org", "9000000001"));
			Account saved = repository.save(newAccount(customer, "SB-1001", "150.00"));

			// Act
			var found = repository.findById(saved.getId());

			// Assert
			assertThat(found).isPresent();
			assertThat(found.get().getAccountNumber()).isEqualTo("SB-1001");
		}

		@Test
		@DisplayName("find account by account number")
		void findAccountByAccountNumber() {
			// Arrange
			Customer customer = customerRepository.save(newCustomer("Asha Raman", "asha@northernarc.org", "9000000001"));
			repository.save(newAccount(customer, "SB-1001", "150.00"));

			// Act
			var found = repository.findByAccountNumber("SB-1001");

			// Assert
			assertThat(found).isPresent();
			assertThat(found.get().getBalance()).isEqualByComparingTo("150.00");
		}

		@Test
		@DisplayName("return empty when account does not exist")
		void returnEmptyWhenAccountDoesNotExist() {
			// Act
			var found = repository.findByAccountNumber("SB-9999");

			// Assert
			assertThat(found).isEmpty();
		}

		@Test
		@DisplayName("existsByAccountNumber returns true")
		void existsByAccountNumberReturnsTrue() {
			// Arrange
			Customer customer = customerRepository.save(newCustomer("Asha Raman", "asha@northernarc.org", "9000000001"));
			repository.save(newAccount(customer, "SB-1001", "150.00"));

			// Act
			boolean exists = repository.existsByAccountNumber("SB-1001");

			// Assert
			assertThat(exists).isTrue();
		}

		@Test
		@DisplayName("existsByAccountNumber returns false")
		void existsByAccountNumberReturnsFalse() {
			// Act
			boolean exists = repository.existsByAccountNumber("SB-4040");

			// Assert
			assertThat(exists).isFalse();
		}

		@Test
		@DisplayName("find accounts by customer id")
		void findAccountsByCustomerId() {
			// Arrange
			Customer customer = customerRepository.save(newCustomer("Asha Raman", "asha@northernarc.org", "9000000001"));
			repository.save(newAccount(customer, "SB-1001", "150.00"));
			repository.save(newAccount(customer, "SB-1002", "50.00"));

			// Act
			List<Account> accounts = repository.findByCustomerId(customer.getId());

			// Assert
			assertThat(accounts).hasSize(2);
			assertThat(accounts).extracting(Account::getAccountNumber).contains("SB-1001", "SB-1002");
		}

		@Test
		@DisplayName("customer with multiple accounts")
		void customerWithMultipleAccounts() {
			// Arrange
			Customer customer = customerRepository.save(newCustomer("Ravi", "ravi@northernarc.org", "9000000002"));
			repository.save(newAccount(customer, "SB-2001", "10.00"));
			repository.save(newAccount(customer, "SB-2002", "20.00"));
			repository.save(newAccount(customer, "SB-2003", "30.00"));

			// Act
			List<Account> accounts = repository.findByCustomerId(customer.getId());

			// Assert
			assertThat(accounts).hasSize(3);
		}

		@Test
		@DisplayName("customer with no accounts")
		void customerWithNoAccounts() {
			// Arrange
			Customer customer = customerRepository.save(newCustomer("No Account", "none@northernarc.org", "9000000009"));

			// Act
			List<Account> accounts = repository.findByCustomerId(customer.getId());

			// Assert
			assertThat(accounts).isEmpty();
		}
	}

	@Nested
	@DisplayName("Update and Delete")
	class UpdateDeleteTests {

		@Test
		@DisplayName("update account")
		void updateAccount() {
			// Arrange
			Customer customer = customerRepository.save(newCustomer("Asha Raman", "asha@northernarc.org", "9000000001"));
			Account saved = repository.save(newAccount(customer, "SB-1001", "150.00"));
			saved.setBalance(new BigDecimal("175.25"));

			// Act
			Account updated = repository.save(saved);

			// Assert
			assertThat(updated.getBalance()).isEqualByComparingTo("175.25");
		}

		@Test
		@DisplayName("delete account")
		void deleteAccount() {
			// Arrange
			Customer customer = customerRepository.save(newCustomer("Asha Raman", "asha@northernarc.org", "9000000001"));
			Account saved = repository.save(newAccount(customer, "SB-1001", "150.00"));

			// Act
			repository.delete(saved);

			// Assert
			assertThat(repository.findById(saved.getId())).isEmpty();
		}

		@Test
		@DisplayName("verify repository count after save and delete")
		void verifyRepositoryCountAfterSaveAndDelete() {
			// Arrange
			Customer customer = customerRepository.save(newCustomer("Asha Raman", "asha@northernarc.org", "9000000001"));
			Account a1 = repository.save(newAccount(customer, "SB-1001", "10.00"));
			Account a2 = repository.save(newAccount(customer, "SB-1002", "20.00"));

			// Act
			long countAfterSave = repository.count();
			repository.delete(a1);
			repository.delete(a2);
			long countAfterDelete = repository.count();

			// Assert
			assertThat(countAfterSave).isEqualTo(2);
			assertThat(countAfterDelete).isEqualTo(0);
		}
	}

	@Nested
	@DisplayName("Edge Cases")
	class EdgeCaseTests {

		@Test
		@DisplayName("duplicate account number should fail")
		void duplicateAccountNumberShouldFail() {
			// Arrange
			Customer c1 = customerRepository.save(newCustomer("Asha Raman", "asha@northernarc.org", "9000000001"));
			Customer c2 = customerRepository.save(newCustomer("Ravi", "ravi@northernarc.org", "9000000002"));
			repository.saveAndFlush(newAccount(c1, "SB-1001", "100.00"));

			// Act + Assert
			assertThatThrownBy(() -> repository.saveAndFlush(newAccount(c2, "SB-1001", "250.00")))
				.isInstanceOf(DataIntegrityViolationException.class);
		}

		@Test
		@DisplayName("null account number lookup")
		void nullAccountNumberLookup() {
			// Act
			var found = repository.findByAccountNumber(null);

			// Assert
			assertThat(found).isEmpty();
		}

		@Test
		@DisplayName("blank account number lookup")
		void blankAccountNumberLookup() {
			// Act
			var found = repository.findByAccountNumber("   ");

			// Assert
			assertThat(found).isEmpty();
		}
	}

	private static Customer newCustomer(String name, String email, String phone) {
		Customer customer = new Customer();
		customer.setName(name);
		customer.setEmail(email);
		customer.setPhoneNumber(phone);
		customer.setActive(true);
		return customer;
	}

	private static Account newAccount(Customer customer, String number, String balance) {
		Account account = new Account();
		account.setCustomer(customer);
		account.setAccountNumber(number);
		account.setBalance(new BigDecimal(balance));
		account.setActive(true);
		return account;
	}
}
