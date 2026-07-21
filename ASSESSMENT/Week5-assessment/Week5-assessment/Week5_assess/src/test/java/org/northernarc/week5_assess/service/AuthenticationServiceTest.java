package org.northernarc.week5_assess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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
import org.northernarc.week5_assess.exception.DuplicateEmailException;
import org.northernarc.week5_assess.exception.InvalidCredentialsException;
import org.northernarc.week5_assess.repository.CustomerRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticationServiceTest")
class AuthenticationServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Nested
    @DisplayName("Registration")
    class RegistrationTests {

        @Test
        @DisplayName("should register successfully")
        void shouldRegisterSuccessfully() {
            // Arrange
            RegisterRequest request = registerRequest("Asha", "asha@northernarc.org", "StrongPass@123", "9000000001");
            when(customerRepository.existsByEmail(request.getEmail())).thenReturn(false);
            when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded-pass");
            when(customerRepository.save(any(Customer.class))).thenReturn(customer(1L, request.getEmail(), "encoded-pass"));

            // Act
            AuthResponse actual = authenticationService.register(request);

            // Assert
            assertThat(actual).isNotNull();
            verify(customerRepository).existsByEmail(request.getEmail());
            verify(passwordEncoder).encode(request.getPassword());
            verify(customerRepository).save(any(Customer.class));
            verifyNoMoreInteractions(customerRepository, passwordEncoder);
        }

        @Test
        @DisplayName("should fail for duplicate email")
        void shouldFailForDuplicateEmail() {
            // Arrange
            RegisterRequest request = registerRequest("Asha", "asha@northernarc.org", "StrongPass@123", "9000000001");
            when(customerRepository.existsByEmail(request.getEmail())).thenReturn(true);

            // Act + Assert
            assertThatThrownBy(() -> authenticationService.register(request))
                .isInstanceOf(DuplicateEmailException.class);
            verify(customerRepository, never()).save(any(Customer.class));
        }

        @Test
        @DisplayName("should encrypt password before saving")
        void shouldEncryptPasswordBeforeSaving() {
            // Arrange
            RegisterRequest request = registerRequest("Asha", "asha@northernarc.org", "StrongPass@123", "9000000001");
            when(customerRepository.existsByEmail(request.getEmail())).thenReturn(false);
            when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded-pass");
            when(customerRepository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));
            ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);

            // Act
            authenticationService.register(request);

            // Assert
            verify(customerRepository).save(captor.capture());
            assertThat(captor.getValue().getPassword()).isEqualTo("encoded-pass");
        }

        @Test
        @DisplayName("should invoke password encoder once")
        void shouldInvokePasswordEncoderOnce() {
            // Arrange
            RegisterRequest request = registerRequest("Asha", "asha@northernarc.org", "StrongPass@123", "9000000001");
            when(customerRepository.existsByEmail(request.getEmail())).thenReturn(false);
            when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded-pass");
            when(customerRepository.save(any(Customer.class))).thenReturn(customer(1L, request.getEmail(), "encoded-pass"));

            // Act
            authenticationService.register(request);

            // Assert
            verify(passwordEncoder, times(1)).encode(request.getPassword());
        }

        @Test
        @DisplayName("should invoke repository save once")
        void shouldInvokeRepositorySaveOnce() {
            // Arrange
            RegisterRequest request = registerRequest("Asha", "asha@northernarc.org", "StrongPass@123", "9000000001");
            when(customerRepository.existsByEmail(request.getEmail())).thenReturn(false);
            when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded-pass");
            when(customerRepository.save(any(Customer.class))).thenReturn(customer(1L, request.getEmail(), "encoded-pass"));

            // Act
            authenticationService.register(request);

            // Assert
            verify(customerRepository, times(1)).save(any(Customer.class));
        }
    }

    @Nested
    @DisplayName("Login")
    class LoginTests {

        @Test
        @DisplayName("should login successfully")
        void shouldLoginSuccessfully() {
            // Arrange
            LoginRequest request = loginRequest("asha@northernarc.org", "StrongPass@123");
            Customer existing = customer(1L, request.getEmail(), "encoded-pass");
            when(customerRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existing));
            when(passwordEncoder.matches(request.getPassword(), existing.getPassword())).thenReturn(true);
            when(jwtService.generateToken(existing)).thenReturn("jwt-token");

            // Act
            AuthResponse actual = authenticationService.login(request);

            // Assert
            assertThat(actual.getToken()).isEqualTo("jwt-token");
            verify(authenticationManager).authenticate(any());
            verify(customerRepository).findByEmail(request.getEmail());
            verify(jwtService).generateToken(existing);
        }

        @Test
        @DisplayName("should fail for invalid password")
        void shouldFailForInvalidPassword() {
            // Arrange
            LoginRequest request = loginRequest("asha@northernarc.org", "WrongPass");
            Customer existing = customer(1L, request.getEmail(), "encoded-pass");
            when(customerRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existing));
            when(passwordEncoder.matches(request.getPassword(), existing.getPassword())).thenReturn(false);

            // Act + Assert
            assertThatThrownBy(() -> authenticationService.login(request))
                .isInstanceOf(InvalidCredentialsException.class);
            verify(jwtService, never()).generateToken(any(Customer.class));
        }

        @Test
        @DisplayName("should fail for invalid email")
        void shouldFailForInvalidEmail() {
            // Arrange
            LoginRequest request = loginRequest("bad-email", "StrongPass@123");

            // Act + Assert
            assertThatThrownBy(() -> authenticationService.login(request))
                .isInstanceOf(IllegalArgumentException.class);
            verify(customerRepository, never()).findByEmail(any());
        }

        @Test
        @DisplayName("should fail when user not found")
        void shouldFailWhenUserNotFound() {
            // Arrange
            LoginRequest request = loginRequest("missing@northernarc.org", "StrongPass@123");
            when(customerRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

            // Act + Assert
            assertThatThrownBy(() -> authenticationService.login(request))
                .isInstanceOf(InvalidCredentialsException.class);
            verify(jwtService, never()).generateToken(any(Customer.class));
        }

        @Test
        @DisplayName("should generate jwt successfully")
        void shouldGenerateJwtSuccessfully() {
            // Arrange
            LoginRequest request = loginRequest("asha@northernarc.org", "StrongPass@123");
            Customer existing = customer(1L, request.getEmail(), "encoded-pass");
            when(customerRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existing));
            when(passwordEncoder.matches(request.getPassword(), existing.getPassword())).thenReturn(true);
            when(jwtService.generateToken(existing)).thenReturn("generated-jwt");

            // Act
            AuthResponse actual = authenticationService.login(request);

            // Assert
            assertThat(actual.getToken()).isEqualTo("generated-jwt");
            verify(jwtService).generateToken(existing);
        }

        @Test
        @DisplayName("should return jwt in response")
        void shouldReturnJwtInResponse() {
            // Arrange
            LoginRequest request = loginRequest("asha@northernarc.org", "StrongPass@123");
            Customer existing = customer(1L, request.getEmail(), "encoded-pass");
            when(customerRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existing));
            when(passwordEncoder.matches(request.getPassword(), existing.getPassword())).thenReturn(true);
            when(jwtService.generateToken(existing)).thenReturn("jwt-response-token");

            // Act
            AuthResponse actual = authenticationService.login(request);

            // Assert
            assertThat(actual).isNotNull();
            assertThat(actual.getToken()).isEqualTo("jwt-response-token");
        }

        @Test
        @DisplayName("should invoke authentication manager correctly")
        void shouldInvokeAuthenticationManagerCorrectly() {
            // Arrange
            LoginRequest request = loginRequest("asha@northernarc.org", "StrongPass@123");
            Customer existing = customer(1L, request.getEmail(), "encoded-pass");
            when(customerRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existing));
            when(passwordEncoder.matches(request.getPassword(), existing.getPassword())).thenReturn(true);
            when(jwtService.generateToken(existing)).thenReturn("jwt-token");

            // Act
            authenticationService.login(request);

            // Assert
            verify(authenticationManager, times(1)).authenticate(any());
            verifyNoMoreInteractions(authenticationManager);
        }
    }

    private static RegisterRequest registerRequest(String name, String email, String password, String phone) {
        RegisterRequest request = new RegisterRequest();
        request.setName(name);
        request.setEmail(email);
        request.setPassword(password);
        request.setPhoneNumber(phone);
        return request;
    }

    private static LoginRequest loginRequest(String email, String password) {
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);
        return request;
    }

    private static Customer customer(Long id, String email, String password) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName("Asha Raman");
        customer.setEmail(email);
        customer.setPassword(password);
        customer.setPhoneNumber("9000000001");
        return customer;
    }
}

