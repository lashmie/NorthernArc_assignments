package org.northernarc.week5_assess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.northernarc.week5_assess.entity.Customer;
import org.northernarc.week5_assess.exception.CustomerNotFoundException;
import org.northernarc.week5_assess.exception.DuplicateEmailException;
import org.northernarc.week5_assess.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomerServiceTest")
class CustomerServiceTest {

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private CustomerServiceImpl customerService;

	@Nested
	@DisplayName("Customer Registration")
	class RegistrationTests {

		@Test
		@DisplayName("should register customer successfully")
		void shouldRegisterCustomerSuccessfully() {
			// Arrange
			Customer request = customerWithoutId();
			Customer saved = customerWithId(101L);
			when(customerRepository.existsByEmail(request.getEmail())).thenReturn(false);
			when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded-pass");
			when(customerRepository.save(any(Customer.class))).thenReturn(saved);

			// Act
			Customer actual = customerService.registerCustomer(request);

			// Assert
			assertThat(actual.getId()).isEqualTo(101L);
			verify(customerRepository).existsByEmail(request.getEmail());
			verify(passwordEncoder).encode(request.getPassword());
			verify(customerRepository).save(any(Customer.class));
			verifyNoMoreInteractions(customerRepository, passwordEncoder);
		}

		@Test
		@DisplayName("should encrypt password before saving")
		void shouldEncryptPasswordBeforeSaving() {
			// Arrange
			Customer request = customerWithoutId();
			when(customerRepository.existsByEmail(request.getEmail())).thenReturn(false);
			when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded-pass");
			when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

			ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);

			// Act
			customerService.registerCustomer(request);

			// Assert
			verify(customerRepository).save(captor.capture());
			assertThat(captor.getValue().getPassword()).isEqualTo("encoded-pass");
		}

		@Test
		@DisplayName("should call password encoder exactly once")
		void shouldCallPasswordEncoderExactlyOnce() {
			// Arrange
			Customer request = customerWithoutId();
			when(customerRepository.existsByEmail(request.getEmail())).thenReturn(false);
			when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded-pass");
			when(customerRepository.save(any(Customer.class))).thenReturn(customerWithId(101L));

			// Act
			customerService.registerCustomer(request);

			// Assert
			verify(passwordEncoder, times(1)).encode(request.getPassword());
		}

		@Test
		@DisplayName("should call repository save once")
		void shouldCallRepositorySaveOnce() {
			// Arrange
			Customer request = customerWithoutId();
			when(customerRepository.existsByEmail(request.getEmail())).thenReturn(false);
			when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded-pass");
			when(customerRepository.save(any(Customer.class))).thenReturn(customerWithId(101L));

			// Act
			customerService.registerCustomer(request);

			// Assert
			verify(customerRepository, times(1)).save(any(Customer.class));
		}

		@Test
		@DisplayName("should throw duplicate email exception when email already exists")
		void shouldThrowDuplicateEmailException() {
			// Arrange
			Customer request = customerWithoutId();
			when(customerRepository.existsByEmail(request.getEmail())).thenReturn(true);

			// Act + Assert
			assertThatThrownBy(() -> customerService.registerCustomer(request))
				.isInstanceOf(DuplicateEmailException.class);
			verify(customerRepository, never()).save(any(Customer.class));
		}

		@Test
		@DisplayName("should fail when customer name is mandatory")
		void shouldFailWhenNameMandatory() {
			Customer request = customerWithoutId();
			request.setName(null);

			assertThatThrownBy(() -> customerService.registerCustomer(request))
				.isInstanceOf(IllegalArgumentException.class);
			verify(customerRepository, never()).save(any(Customer.class));
		}

		@Test
		@DisplayName("should fail when email is mandatory")
		void shouldFailWhenEmailMandatory() {
			Customer request = customerWithoutId();
			request.setEmail(null);

			assertThatThrownBy(() -> customerService.registerCustomer(request))
				.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		@DisplayName("should fail when password is mandatory")
		void shouldFailWhenPasswordMandatory() {
			Customer request = customerWithoutId();
			request.setPassword(null);

			assertThatThrownBy(() -> customerService.registerCustomer(request))
				.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		@DisplayName("should fail when phone number is mandatory")
		void shouldFailWhenPhoneMandatory() {
			Customer request = customerWithoutId();
			request.setPhoneNumber(null);

			assertThatThrownBy(() -> customerService.registerCustomer(request))
				.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		@DisplayName("should fail for invalid email format")
		void shouldFailForInvalidEmailFormat() {
			Customer request = customerWithoutId();
			request.setEmail("invalid-email");

			assertThatThrownBy(() -> customerService.registerCustomer(request))
				.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		@DisplayName("should fail when phone number less than 10 digits")
		void shouldFailWhenPhoneLessThan10() {
			Customer request = customerWithoutId();
			request.setPhoneNumber("12345");

			assertThatThrownBy(() -> customerService.registerCustomer(request))
				.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		@DisplayName("should fail when phone number greater than 10 digits")
		void shouldFailWhenPhoneGreaterThan10() {
			Customer request = customerWithoutId();
			request.setPhoneNumber("123456789012");

			assertThatThrownBy(() -> customerService.registerCustomer(request))
				.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		@DisplayName("should fail when phone number contains alphabets")
		void shouldFailWhenPhoneContainsAlphabets() {
			Customer request = customerWithoutId();
			request.setPhoneNumber("12345abcde");

			assertThatThrownBy(() -> customerService.registerCustomer(request))
				.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		@DisplayName("should fail when phone number contains special characters")
		void shouldFailWhenPhoneContainsSpecialChars() {
			Customer request = customerWithoutId();
			request.setPhoneNumber("12345-7890");

			assertThatThrownBy(() -> customerService.registerCustomer(request))
				.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		@DisplayName("should fail when blank name")
		void shouldFailWhenBlankName() {
			Customer request = customerWithoutId();
			request.setName("   ");

			assertThatThrownBy(() -> customerService.registerCustomer(request))
				.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		@DisplayName("should fail when blank email")
		void shouldFailWhenBlankEmail() {
			Customer request = customerWithoutId();
			request.setEmail("   ");

			assertThatThrownBy(() -> customerService.registerCustomer(request))
				.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		@DisplayName("should fail when blank password")
		void shouldFailWhenBlankPassword() {
			Customer request = customerWithoutId();
			request.setPassword("   ");

			assertThatThrownBy(() -> customerService.registerCustomer(request))
				.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		@DisplayName("should fail when request object is null")
		void shouldFailWhenRequestIsNull() {
			assertThatThrownBy(() -> customerService.registerCustomer(null))
				.isInstanceOf(IllegalArgumentException.class);
			verify(customerRepository, never()).save(any(Customer.class));
		}

		@Test
		@DisplayName("should handle leading and trailing spaces in input")
		void shouldHandleLeadingAndTrailingSpaces() {
			// Arrange
			Customer request = customerWithoutId();
			request.setName("  Asha Raman  ");
			request.setEmail("  asha@northernarc.org  ");
			request.setPhoneNumber(" 9000000001 ");
			when(customerRepository.existsByEmail("asha@northernarc.org")).thenReturn(false);
			when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded-pass");
			when(customerRepository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));

			ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);

			// Act
			customerService.registerCustomer(request);

			// Assert
			verify(customerRepository).save(captor.capture());
			assertThat(captor.getValue().getName()).isEqualTo("Asha Raman");
			assertThat(captor.getValue().getEmail()).isEqualTo("asha@northernarc.org");
		}
	}

	@Nested
	@DisplayName("Customer CRUD")
	class CrudTests {

		@Test
		@DisplayName("should get all customers successfully")
		void shouldGetAllCustomersSuccessfully() {
			// Arrange
			when(customerRepository.findAll()).thenReturn(List.of(customerWithId(1L), customerWithId(2L)));

			// Act
			List<Customer> actual = customerService.getAllCustomers();

			// Assert
			assertThat(actual).hasSize(2);
			verify(customerRepository).findAll();
			verifyNoMoreInteractions(customerRepository);
		}

		@Test
		@DisplayName("should get customer by id successfully")
		void shouldGetCustomerByIdSuccessfully() {
			// Arrange
			when(customerRepository.findById(11L)).thenReturn(Optional.of(customerWithId(11L)));

			// Act
			Customer actual = customerService.getCustomerById(11L);

			// Assert
			assertThat(actual.getId()).isEqualTo(11L);
			verify(customerRepository).findById(11L);
		}

		@Test
		@DisplayName("should throw customer not found for invalid id")
		void shouldThrowNotFoundForInvalidId() {
			// Arrange
			when(customerRepository.findById(999L)).thenReturn(Optional.empty());

			// Act + Assert
			assertThatThrownBy(() -> customerService.getCustomerById(999L))
				.isInstanceOf(CustomerNotFoundException.class);
			verify(customerRepository).findById(999L);
		}

		@Test
		@DisplayName("should update customer successfully")
		void shouldUpdateCustomerSuccessfully() {
			// Arrange
			Customer existing = customerWithId(10L);
			Customer update = customerWithoutId();
			update.setName("Updated Name");
			when(customerRepository.findById(10L)).thenReturn(Optional.of(existing));
			when(customerRepository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));

			// Act
			Customer updated = customerService.updateCustomer(10L, update);

			// Assert
			assertThat(updated.getName()).isEqualTo("Updated Name");
			verify(customerRepository).findById(10L);
			verify(customerRepository).save(existing);
		}

		@Test
		@DisplayName("should throw customer not found when updating non existing")
		void shouldThrowNotFoundWhenUpdatingNonExisting() {
			// Arrange
			when(customerRepository.findById(404L)).thenReturn(Optional.empty());

			// Act + Assert
			assertThatThrownBy(() -> customerService.updateCustomer(404L, customerWithoutId()))
				.isInstanceOf(CustomerNotFoundException.class);
			verify(customerRepository, never()).save(any(Customer.class));
		}

		@Test
		@DisplayName("should delete customer successfully")
		void shouldDeleteCustomerSuccessfully() {
			// Arrange
			Customer existing = customerWithId(7L);
			when(customerRepository.findById(7L)).thenReturn(Optional.of(existing));

			// Act
			customerService.deleteCustomer(7L);

			// Assert
			verify(customerRepository).findById(7L);
			verify(customerRepository).delete(existing);
		}

		@Test
		@DisplayName("should throw customer not found when deleting non existing")
		void shouldThrowNotFoundWhenDeletingNonExisting() {
			// Arrange
			when(customerRepository.findById(88L)).thenReturn(Optional.empty());

			// Act + Assert
			assertThatThrownBy(() -> customerService.deleteCustomer(88L))
				.isInstanceOf(CustomerNotFoundException.class);
			verify(customerRepository, never()).delete(any(Customer.class));
		}
	}

	private static Customer customerWithoutId() {
		Customer c = new Customer();
		c.setName("Asha Raman");
		c.setEmail("asha@northernarc.org");
		c.setPassword("StrongPass@123");
		c.setPhoneNumber("9000000001");
		return c;
	}

	private static Customer customerWithId(Long id) {
		Customer c = customerWithoutId();
		c.setId(id);
		return c;
	}
}
