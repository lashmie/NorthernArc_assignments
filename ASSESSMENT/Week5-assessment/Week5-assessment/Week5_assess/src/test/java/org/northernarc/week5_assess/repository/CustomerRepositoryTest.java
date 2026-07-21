package org.northernarc.week5_assess.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.northernarc.week5_assess.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
@DisplayName("CustomerRepository - DataJpa Tests")
class CustomerRepositoryTest {

	@Autowired
	private CustomerRepository repository;

	@Nested
	@DisplayName("Create and Read")
	class CreateReadTests {

		@Test
		@DisplayName("save customer successfully")
		void saveCustomerSuccessfully() {
			// Arrange
			Customer customer = newCustomer("Asha Raman", "asha@northernarc.org", "9000000001");

			// Act
			Customer saved = repository.save(customer);

			// Assert
			assertThat(saved.getId()).isNotNull();
			assertThat(saved.getEmail()).isEqualTo("asha@northernarc.org");
			assertThat(repository.count()).isEqualTo(1);
		}

		@Test
		@DisplayName("find customer by id")
		void findCustomerById() {
			// Arrange
			Customer saved = repository.save(newCustomer("Asha Raman", "asha@northernarc.org", "9000000001"));

			// Act
			var found = repository.findById(saved.getId());

			// Assert
			assertThat(found).isPresent();
			assertThat(found.get().getId()).isEqualTo(saved.getId());
			assertThat(found.get().getName()).isEqualTo("Asha Raman");
		}

		@Test
		@DisplayName("find customer by email")
		void findCustomerByEmail() {
			// Arrange
			repository.save(newCustomer("Asha Raman", "asha@northernarc.org", "9000000001"));

			// Act
			var found = repository.findByEmail("asha@northernarc.org");

			// Assert
			assertThat(found).isPresent();
			assertThat(found.get().getName()).isEqualTo("Asha Raman");
		}

		@Test
		@DisplayName("return empty when customer id does not exist")
		void returnEmptyWhenCustomerIdDoesNotExist() {
			// Act
			var found = repository.findById(99999L);

			// Assert
			assertThat(found).isEmpty();
		}

		@Test
		@DisplayName("return empty when email does not exist")
		void returnEmptyWhenEmailDoesNotExist() {
			// Act
			var found = repository.findByEmail("missing@northernarc.org");

			// Assert
			assertThat(found).isEmpty();
		}

		@Test
		@DisplayName("existsByEmail returns true")
		void existsByEmailReturnsTrue() {
			// Arrange
			repository.save(newCustomer("Asha Raman", "asha@northernarc.org", "9000000001"));

			// Act
			boolean exists = repository.existsByEmail("asha@northernarc.org");

			// Assert
			assertThat(exists).isTrue();
		}

		@Test
		@DisplayName("existsByEmail returns false")
		void existsByEmailReturnsFalse() {
			// Act
			boolean exists = repository.existsByEmail("notfound@northernarc.org");

			// Assert
			assertThat(exists).isFalse();
		}
	}

	@Nested
	@DisplayName("Update and Delete")
	class UpdateDeleteTests {

		@Test
		@DisplayName("update customer details")
		void updateCustomerDetails() {
			// Arrange
			Customer saved = repository.save(newCustomer("Asha Raman", "asha@northernarc.org", "9000000001"));
			saved.setName("Asha R");
			saved.setPhoneNumber("9111111111");

			// Act
			Customer updated = repository.save(saved);

			// Assert
			assertThat(updated.getName()).isEqualTo("Asha R");
			assertThat(updated.getPhoneNumber()).isEqualTo("9111111111");
		}

		@Test
		@DisplayName("delete customer")
		void deleteCustomer() {
			// Arrange
			Customer saved = repository.save(newCustomer("Asha Raman", "asha@northernarc.org", "9000000001"));

			// Act
			repository.delete(saved);

			// Assert
			assertThat(repository.findById(saved.getId())).isEmpty();
		}

		@Test
		@DisplayName("verify repository count after save and delete")
		void verifyRepositoryCountAfterSaveAndDelete() {
			// Arrange
			Customer c1 = repository.save(newCustomer("Asha Raman", "asha@northernarc.org", "9000000001"));
			Customer c2 = repository.save(newCustomer("Ravi Kumar", "ravi@northernarc.org", "9000000002"));

			// Act
			long countAfterSave = repository.count();
			repository.delete(c1);
			repository.delete(c2);
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
		@DisplayName("duplicate email should fail unique constraint")
		void duplicateEmailShouldFail() {
			// Arrange
			repository.saveAndFlush(newCustomer("Asha Raman", "asha@northernarc.org", "9000000001"));

			// Act + Assert
			assertThatThrownBy(() -> repository.saveAndFlush(newCustomer("Asha Two", "asha@northernarc.org", "9000000009")))
				.isInstanceOf(DataIntegrityViolationException.class);
		}

		@Test
		@DisplayName("null email lookup")
		void nullEmailLookup() {
			// Act
			var found = repository.findByEmail(null);

			// Assert
			assertThat(found).isEmpty();
		}

		@Test
		@DisplayName("blank email lookup")
		void blankEmailLookup() {
			// Act
			var found = repository.findByEmail("   ");

			// Assert
			assertThat(found).isEmpty();
		}

		@Test
		@DisplayName("multiple customers in database")
		void multipleCustomersInDatabase() {
			// Arrange
			repository.save(newCustomer("Asha Raman", "asha@northernarc.org", "9000000001"));
			repository.save(newCustomer("Ravi Kumar", "ravi@northernarc.org", "9000000002"));
			repository.save(newCustomer("Nila S", "nila@northernarc.org", "9000000003"));

			// Act
			long total = repository.count();

			// Assert
			assertThat(total).isEqualTo(3);
			assertThat(repository.findByEmail("ravi@northernarc.org")).isPresent();
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
}
