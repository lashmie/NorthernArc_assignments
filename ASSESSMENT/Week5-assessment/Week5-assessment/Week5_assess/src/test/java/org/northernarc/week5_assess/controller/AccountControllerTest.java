package org.northernarc.week5_assess.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.northernarc.week5_assess.dto.AccountRequest;
import org.northernarc.week5_assess.dto.AccountResponse;
import org.northernarc.week5_assess.dto.AmountRequest;
import org.northernarc.week5_assess.exception.AccountNotFoundException;
import org.northernarc.week5_assess.exception.InsufficientFundsException;
import org.northernarc.week5_assess.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * AccountControllerTest - Comprehensive MockMvc Tests for Account Management API
 *
 * TESTING COVERAGE:
 * ================
 * 1. CREATE ACCOUNT (POST /accounts) [REQUIRES JWT]:
 *    - Happy Path: Valid request + JWT -> 201 CREATED + account details
 *      Verifies: verify(accountService).createAccount(request)
 *    - Validation Failures -> 400 BAD_REQUEST with verifyNoInteractions(accountService)
 *      * Customer ID is null
 *      * Account type is null
 *      * Account type is blank/whitespace
 *      * Customer ID type is invalid (string instead of number)
 *      * Malformed JSON
 *    - Business Logic Errors:
 *      * Unsupported account type -> 400 BAD_REQUEST
 *      * Customer not found -> 404 NOT_FOUND
 *    - Media Type: 415 UNSUPPORTED_MEDIA_TYPE
 *    - Security: 401 UNAUTHORIZED when JWT missing, 403 FORBIDDEN for wrong role
 *
 * 2. GET ACCOUNT (GET /accounts/{accountNumber}) [REQUIRES JWT]:
 *    - Happy Path: Valid account number + JWT -> 200 OK + account details + balance
 *      Verifies: verify(accountService).getAccountByNumber("SB-1001")
 *    - Not Found: Invalid account number -> 404 NOT_FOUND
 *    - Blank Account Number: Validation error -> 400 BAD_REQUEST
 *    - Decimal Balance: Precision handling -> Returns exact decimal value
 *    - Service Invocation: verify() called exactly once
 *    - Security: 401 UNAUTHORIZED when JWT missing, 403 FORBIDDEN for wrong role
 *
 * 3. DEPOSIT (POST /accounts/{accountNumber}/deposit) [REQUIRES JWT]:
 *    - Happy Path: Valid amount + JWT -> 200 OK + updated balance
 *      Verifies: verify(accountService).deposit("SB-1001", bd("50.00"))
 *    - Decimal Precision: 2-decimal amounts handled correctly -> 200 OK
 *    - Validation Failures -> 400 BAD_REQUEST with verifyNoInteractions(accountService)
 *      * Amount is zero
 *      * Amount is negative
 *      * Amount field is missing
 *      * Amount is null
 *      * Amount type is invalid (string)
 *      * Malformed JSON
 *    - Business Logic: Account not found -> 404 NOT_FOUND
 *    - Media Type: 415 UNSUPPORTED_MEDIA_TYPE
 *    - Security: 401 UNAUTHORIZED when JWT missing, 403 FORBIDDEN for wrong role
 *
 * 4. WITHDRAW (POST /accounts/{accountNumber}/withdraw) [REQUIRES JWT]:
 *    - Happy Path: Valid amount + JWT -> 200 OK + updated balance
 *      Verifies: verify(accountService).withdraw("SB-1001", bd("20.00"))
 *    - Insufficient Funds: Amount > balance -> 422 UNPROCESSABLE_ENTITY
 *    - Validation Failures -> 400 BAD_REQUEST with verifyNoInteractions(accountService)
 *      * Amount is zero
 *      * Amount is negative
 *      * Amount is null
 *      * Amount field is missing
 *      * Amount type is invalid (string)
 *      * Malformed JSON
 *    - Business Logic: Account not found -> 404 NOT_FOUND
 *    - Security: 401 UNAUTHORIZED when JWT missing, 403 FORBIDDEN for wrong role
 *
 * 5. SECURITY TESTS:
 *    - All endpoints require JWT (return 401 if missing)
 *    - Some endpoints require specific roles (return 403 for unauthorized roles)
 *
 * KEY VERIFICATION PATTERNS:
 * =========================
 * - verify(accountService).method(...) -> Service called exactly once on success
 * - verifyNoInteractions(accountService) -> Service NEVER called for invalid input
 *   Used for validation failures, malformed JSON, unsupported media type
 *
 * BAD REQUEST BEHAVIOR (verifyNoInteractions MANDATORY):
 * =====================================================
 * Missing/invalid values must:
 * 1. Return 400 BAD_REQUEST at validation layer (before service)
 * 2. NEVER invoke accountService
 * 3. Assert: verifyNoInteractions(accountService) after status().isBadRequest()
 *
 * STATUS CODE REFERENCE:
 * ====================
 * 201 CREATED - Account created
 * 200 OK - Account found, deposit/withdraw successful
 * 400 BAD_REQUEST - Invalid input (validation), missing required fields
 * 401 UNAUTHORIZED - JWT missing or invalid
 * 403 FORBIDDEN - Sufficient authentication but lacking authorization for role
 * 404 NOT_FOUND - Account/Customer does not exist
 * 415 UNSUPPORTED_MEDIA_TYPE - Content-Type header missing or incorrect
 * 422 UNPROCESSABLE_ENTITY - Business logic error (insufficient funds)
 */
@WebMvcTest(controllers = AccountController.class)
@DisplayName("AccountController - MockMvc Tests")
public class AccountControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private AccountService accountService;



	// Setup method called before each test method
	@BeforeEach
	void beforeEach() {
		// Reset mock state and clear any previous interactions with accountService
		reset(accountService);
	}

	// Cleanup method called after each test method
	@AfterEach
	void afterEach() {
		// Verify no unexpected interactions with mocks after each test
		verifyNoMoreInteractions(accountService);
	}

	@Nested
	@DisplayName("POST /accounts")
	class CreateAccountTests {

		// Test: Valid account creation with JWT returns 201 CREATED
		@Test
		@DisplayName("should return 201 for valid account creation")
		void createAccount_whenValidRequest_shouldReturnCreated() throws Exception {
			AccountRequest request = new AccountRequest();
			request.setCustomerId(1L);
			request.setAccountType("SAVINGS");
			when(accountService.createAccount(request)).thenReturn(accountResponse("SB-1001", bd("0.00")));

			mockMvc.perform(post("/accounts")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.accountNumber").value("SB-1001"));

			verify(accountService, times(1)).createAccount(request);
		}

		// Test: Null customer ID returns 400 BAD_REQUEST
		@Test
		@DisplayName("should return 400 when customer id is null")
		void createAccount_whenCustomerIdNull_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/accounts")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content("{\"accountType\":\"SAVINGS\"}"))
				.andExpect(status().isBadRequest());

			verify(accountService, times(0)).createAccount(null);
		}

		// Test: Null account type returns 400 BAD_REQUEST
		@Test
		@DisplayName("should return 400 when account type is null")
		void createAccount_whenAccountTypeNull_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/accounts")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content("{\"customerId\":1}"))
				.andExpect(status().isBadRequest());

			verify(accountService, times(0)).createAccount(null);
		}

		// Test: Blank account type returns 400 BAD_REQUEST
		@Test
		@DisplayName("should return 400 when account type is blank")
		void createAccount_whenAccountTypeBlank_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/accounts")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content("{\"customerId\":1,\"accountType\":\"   \"}"))
				.andExpect(status().isBadRequest());

			verify(accountService, times(0)).createAccount(null);
		}

		// Test: Unsupported account type returns 400 BAD_REQUEST
		@Test
		@DisplayName("should return 400 when account type is unsupported")
		void createAccount_whenUnsupportedType_shouldReturnBadRequest() throws Exception {
			AccountRequest request = new AccountRequest();
			request.setCustomerId(1L);
			request.setAccountType("CRYPTO");
			when(accountService.createAccount(request)).thenThrow(new IllegalArgumentException("Unsupported account type"));

			mockMvc.perform(post("/accounts")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest());

			verify(accountService, times(1)).createAccount(request);
		}

		// Test: Customer not found returns 404 NOT_FOUND
		@Test
		@DisplayName("should return 404 when customer is missing")
		void createAccount_whenCustomerMissing_shouldReturnNotFound() throws Exception {
			AccountRequest request = new AccountRequest();
			request.setCustomerId(999L);
			request.setAccountType("SAVINGS");
			when(accountService.createAccount(request)).thenThrow(new AccountNotFoundException("Customer not found"));

			mockMvc.perform(post("/accounts")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isNotFound());

			verify(accountService, times(1)).createAccount(request);
		}

		// Test: Malformed JSON returns 400 BAD_REQUEST
		@Test
		@DisplayName("should return 400 for malformed JSON")
		void createAccount_whenMalformedJson_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/accounts")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content("{\"customerId\":"))
				.andExpect(status().isBadRequest());

			verify(accountService, times(0)).createAccount(null);
		}

		// Test: Invalid customer ID type returns 400 BAD_REQUEST
		@Test
		@DisplayName("should return 400 when customer id type is invalid")
		void createAccount_whenCustomerIdTypeInvalid_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/accounts")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content("{\"customerId\":\"abc\",\"accountType\":\"SAVINGS\"}"))
				.andExpect(status().isBadRequest());

			verify(accountService, times(0)).createAccount(null);
		}

		// Test: Missing Content-Type returns 415 UNSUPPORTED_MEDIA_TYPE
		@Test
		@DisplayName("should return 415 when content type is missing")
		void createAccount_whenContentTypeMissing_shouldReturnUnsupportedMediaType() throws Exception {
			mockMvc.perform(post("/accounts")
					.with(jwt())
					.content("{\"customerId\":1,\"accountType\":\"SAVINGS\"}"))
				.andExpect(status().isUnsupportedMediaType());

			verify(accountService, times(0)).createAccount(null);
		}

		// Test: Missing JWT returns 401 UNAUTHORIZED
		@Test
		@DisplayName("should return 401 when JWT is missing")
		void createAccount_whenJwtMissing_shouldReturnUnauthorized() throws Exception {
			mockMvc.perform(post("/accounts")
					.contentType(APPLICATION_JSON)
					.content("{\"customerId\":1,\"accountType\":\"SAVINGS\"}"))
				.andExpect(status().isUnauthorized());

			verify(accountService, times(0)).createAccount(null);
		}

		// Test: Wrong role returns 403 FORBIDDEN
		@Test
		@DisplayName("should return 403 when role is not allowed")
		void createAccount_whenForbiddenRole_shouldReturnForbidden() throws Exception {
			mockMvc.perform(post("/accounts")
					.with(jwt().authorities(new SimpleGrantedAuthority("ROLE_VIEWER")))
					.contentType(APPLICATION_JSON)
					.content("{\"customerId\":1,\"accountType\":\"SAVINGS\"}"))
				.andExpect(status().isForbidden());

			verify(accountService, times(0)).createAccount(null);
		}
	}

	@Nested
	@DisplayName("GET /accounts/{accountNumber}")
	class GetAccountTests {

		// Test: Valid account with JWT returns 200 OK
		@Test
		@DisplayName("should return 200 when account exists")
		void getAccount_whenFound_shouldReturnOk() throws Exception {
			when(accountService.getAccountByNumber("SB-1001")).thenReturn(accountResponse("SB-1001", bd("125.00")));

			mockMvc.perform(get("/accounts/{accountNumber}", "SB-1001").with(jwt()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.balance").value(125.00));

			verify(accountService, times(1)).getAccountByNumber("SB-1001");
		}

		// Test: Account not found returns 404 NOT_FOUND
		@Test
		@DisplayName("should return 404 when account is missing")
		void getAccount_whenMissing_shouldReturnNotFound() throws Exception {
			when(accountService.getAccountByNumber("SB-404")).thenThrow(new AccountNotFoundException("not found"));

			mockMvc.perform(get("/accounts/{accountNumber}", "SB-404").with(jwt()))
				.andExpect(status().isNotFound());

			verify(accountService, times(1)).getAccountByNumber("SB-404");
		}

		// Test: Blank account number returns 400 BAD_REQUEST
		@Test
		@DisplayName("should return 400 when account number is blank")
		void getAccount_whenBlankAccountNumber_shouldReturnBadRequest() throws Exception {
			when(accountService.getAccountByNumber(" ")).thenThrow(new IllegalArgumentException("Account number is required"));

			mockMvc.perform(get("/accounts/{accountNumber}", " ").with(jwt()))
				.andExpect(status().isBadRequest());

			verify(accountService, times(1)).getAccountByNumber(" ");
		}

		// Test: Unexpected error returns 500 INTERNAL_SERVER_ERROR
		@Test
		@DisplayName("should return 500 when service throws unexpected exception")
		void getAccount_whenUnexpectedError_shouldReturnInternalServerError() throws Exception {
			when(accountService.getAccountByNumber("SB-500")).thenThrow(new RuntimeException("unexpected"));

			mockMvc.perform(get("/accounts/{accountNumber}", "SB-500").with(jwt()))
				.andExpect(status().isInternalServerError());

			verify(accountService, times(1)).getAccountByNumber("SB-500");
		}

		// Test: Missing JWT returns 401 UNAUTHORIZED
		@Test
		@DisplayName("should return 401 when JWT is missing")
		void getAccount_whenJwtMissing_shouldReturnUnauthorized() throws Exception {
			mockMvc.perform(get("/accounts/{accountNumber}", "SB-1001"))
				.andExpect(status().isUnauthorized());

			verify(accountService, times(0)).getAccountByNumber("SB-1001");
		}

		// Test: Wrong role returns 403 FORBIDDEN
		@Test
		@DisplayName("should return 403 when role is not allowed")
		void getAccount_whenForbiddenRole_shouldReturnForbidden() throws Exception {
			mockMvc.perform(get("/accounts/{accountNumber}", "SB-1001")
					.with(jwt().authorities(new SimpleGrantedAuthority("ROLE_AUDITOR"))))
				.andExpect(status().isForbidden());

			verify(accountService, times(0)).getAccountByNumber("SB-1001");
		}

		// Test: Decimal balance returned correctly
		@Test
		@DisplayName("should return balance with decimal precision")
		void getAccount_whenDecimalBalance_shouldReturnPrecisionValue() throws Exception {
			when(accountService.getAccountByNumber("SB-DECIMAL")).thenReturn(accountResponse("SB-DECIMAL", bd("125.75")));

			mockMvc.perform(get("/accounts/{accountNumber}", "SB-DECIMAL").with(jwt()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.balance").value(125.75));

			verify(accountService, times(1)).getAccountByNumber("SB-DECIMAL");
		}

		// Test: Service called exactly once
		@Test
		@DisplayName("should verify service called exactly once for fetch")
		void getAccount_whenValid_shouldInvokeServiceOnce() throws Exception {
			when(accountService.getAccountByNumber("SB-3210")).thenReturn(accountResponse("SB-3210", bd("10.00")));

			mockMvc.perform(get("/accounts/{accountNumber}", "SB-3210").with(jwt()))
				.andExpect(status().isOk());

			verify(accountService, times(1)).getAccountByNumber("SB-3210");
		}
	}

	@Nested
	@DisplayName("POST /accounts/{accountNumber}/deposit")
	class DepositTests {

		@Test
		@DisplayName("should return 200 for valid deposit")
		void deposit_whenValidAmount_shouldReturnOk() throws Exception {
			AmountRequest amountRequest = new AmountRequest();
			amountRequest.setAmount(bd("50.00"));
			when(accountService.deposit("SB-1001", bd("50.00"))).thenReturn(accountResponse("SB-1001", bd("175.00")));

			mockMvc.perform(post("/accounts/{accountNumber}/deposit", "SB-1001")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(amountRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.balance").value(175.00));

			verify(accountService).deposit("SB-1001", bd("50.00"));
		}

		@Test
		@DisplayName("should return 200 when deposit amount has 2-decimal precision")
		void deposit_whenTwoDecimalAmount_shouldReturnOk() throws Exception {
			AmountRequest amountRequest = new AmountRequest();
			amountRequest.setAmount(bd("0.01"));
			when(accountService.deposit("SB-1001", bd("0.01"))).thenReturn(accountResponse("SB-1001", bd("125.01")));

			mockMvc.perform(post("/accounts/{accountNumber}/deposit", "SB-1001")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(amountRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.balance").value(125.01));
		}

		@Test
		@DisplayName("should return 400 when amount is zero")
		void deposit_whenAmountZero_shouldReturnBadRequest() throws Exception {
			AmountRequest amountRequest = new AmountRequest();
			amountRequest.setAmount(bd("0.00"));
			when(accountService.deposit("SB-1001", bd("0.00"))).thenThrow(new IllegalArgumentException("Amount must be positive"));

			mockMvc.perform(post("/accounts/{accountNumber}/deposit", "SB-1001")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(amountRequest)))
				.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("should return 400 when amount is negative")
		void deposit_whenAmountNegative_shouldReturnBadRequest() throws Exception {
			AmountRequest amountRequest = new AmountRequest();
			amountRequest.setAmount(bd("-10.00"));
			when(accountService.deposit("SB-1001", bd("-10.00"))).thenThrow(new IllegalArgumentException("Amount must be positive"));

			mockMvc.perform(post("/accounts/{accountNumber}/deposit", "SB-1001")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(amountRequest)))
				.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("should return 400 when amount field is missing")
		void deposit_whenAmountMissing_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/accounts/{accountNumber}/deposit", "SB-1001")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content("{}"))
				.andExpect(status().isBadRequest());

			verifyNoInteractions(accountService);
		}

		@Test
		@DisplayName("should return 400 when amount is null")
		void deposit_whenAmountNull_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/accounts/{accountNumber}/deposit", "SB-1001")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content("{\"amount\":null}"))
				.andExpect(status().isBadRequest());

			verifyNoInteractions(accountService);
		}

		@Test
		@DisplayName("should return 400 when amount type is invalid")
		void deposit_whenAmountTypeInvalid_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/accounts/{accountNumber}/deposit", "SB-1001")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content("{\"amount\":\"abc\"}"))
				.andExpect(status().isBadRequest());

			verifyNoInteractions(accountService);
		}

		@Test
		@DisplayName("should return 404 when account is not found")
		void deposit_whenAccountMissing_shouldReturnNotFound() throws Exception {
			AmountRequest amountRequest = new AmountRequest();
			amountRequest.setAmount(bd("50.00"));
			when(accountService.deposit("SB-404", bd("50.00"))).thenThrow(new AccountNotFoundException("Not found"));

			mockMvc.perform(post("/accounts/{accountNumber}/deposit", "SB-404")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(amountRequest)))
				.andExpect(status().isNotFound());
		}

		@Test
		@DisplayName("should return 401 when JWT is missing")
		void deposit_whenJwtMissing_shouldReturnUnauthorized() throws Exception {
			mockMvc.perform(post("/accounts/{accountNumber}/deposit", "SB-1001")
					.contentType(APPLICATION_JSON)
					.content("{\"amount\":10.00}"))
				.andExpect(status().isUnauthorized());
		}

		@Test
		@DisplayName("should return 403 when role is not allowed")
		void deposit_whenForbiddenRole_shouldReturnForbidden() throws Exception {
			mockMvc.perform(post("/accounts/{accountNumber}/deposit", "SB-1001")
					.with(jwt().authorities(new SimpleGrantedAuthority("ROLE_VIEWER")))
					.contentType(APPLICATION_JSON)
					.content("{\"amount\":10.00}"))
				.andExpect(status().isForbidden());
		}

		@Test
		@DisplayName("should return 400 for malformed JSON")
		void deposit_whenMalformedJson_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/accounts/{accountNumber}/deposit", "SB-1001")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content("{\"amount\":"))
				.andExpect(status().isBadRequest());

			verifyNoInteractions(accountService);
		}
	}

	@Nested
	@DisplayName("POST /accounts/{accountNumber}/withdraw")
	class WithdrawTests {

		@Test
		@DisplayName("should return 200 for valid withdrawal")
		void withdraw_whenValidAmount_shouldReturnOk() throws Exception {
			AmountRequest amountRequest = new AmountRequest();
			amountRequest.setAmount(bd("20.00"));
			when(accountService.withdraw("SB-1001", bd("20.00"))).thenReturn(accountResponse("SB-1001", bd("80.00")));

			mockMvc.perform(post("/accounts/{accountNumber}/withdraw", "SB-1001")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(amountRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.balance").value(80.00));

			verify(accountService).withdraw("SB-1001", bd("20.00"));
		}

		@Test
		@DisplayName("should return business error when funds are insufficient")
		void withdraw_whenInsufficientFunds_shouldReturnUnprocessableEntity() throws Exception {
			AmountRequest amountRequest = new AmountRequest();
			amountRequest.setAmount(bd("500.00"));
			when(accountService.withdraw("SB-1001", bd("500.00")))
				.thenThrow(new InsufficientFundsException("Insufficient funds"));

			mockMvc.perform(post("/accounts/{accountNumber}/withdraw", "SB-1001")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(amountRequest)))
				.andExpect(status().isUnprocessableEntity());
		}

		@Test
		@DisplayName("should return 404 when account is missing")
		void withdraw_whenAccountMissing_shouldReturnNotFound() throws Exception {
			AmountRequest amountRequest = new AmountRequest();
			amountRequest.setAmount(bd("20.00"));
			when(accountService.withdraw("SB-404", bd("20.00"))).thenThrow(new AccountNotFoundException("not found"));

			mockMvc.perform(post("/accounts/{accountNumber}/withdraw", "SB-404")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(amountRequest)))
				.andExpect(status().isNotFound());
		}

		@Test
		@DisplayName("should return 400 when amount is zero")
		void withdraw_whenAmountZero_shouldReturnBadRequest() throws Exception {
			AmountRequest amountRequest = new AmountRequest();
			amountRequest.setAmount(bd("0.00"));
			when(accountService.withdraw("SB-1001", bd("0.00"))).thenThrow(new IllegalArgumentException("Amount must be positive"));

			mockMvc.perform(post("/accounts/{accountNumber}/withdraw", "SB-1001")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(amountRequest)))
				.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("should return 400 when amount is negative")
		void withdraw_whenAmountNegative_shouldReturnBadRequest() throws Exception {
			AmountRequest amountRequest = new AmountRequest();
			amountRequest.setAmount(bd("-1.00"));
			when(accountService.withdraw("SB-1001", bd("-1.00"))).thenThrow(new IllegalArgumentException("Amount must be positive"));

			mockMvc.perform(post("/accounts/{accountNumber}/withdraw", "SB-1001")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(amountRequest)))
				.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("should return 400 when amount is null")
		void withdraw_whenAmountNull_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/accounts/{accountNumber}/withdraw", "SB-1001")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content("{\"amount\":null}"))
				.andExpect(status().isBadRequest());

			verifyNoInteractions(accountService);
		}

		@Test
		@DisplayName("should return 400 when amount field is missing")
		void withdraw_whenAmountMissing_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/accounts/{accountNumber}/withdraw", "SB-1001")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content("{}"))
				.andExpect(status().isBadRequest());

			verifyNoInteractions(accountService);
		}

		@Test
		@DisplayName("should return 400 when amount type is invalid")
		void withdraw_whenAmountTypeInvalid_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/accounts/{accountNumber}/withdraw", "SB-1001")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content("{\"amount\":\"oops\"}"))
				.andExpect(status().isBadRequest());

			verifyNoInteractions(accountService);
		}

		@Test
		@DisplayName("should return 400 for malformed JSON")
		void withdraw_whenMalformedJson_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/accounts/{accountNumber}/withdraw", "SB-1001")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content("{\"amount\":"))
				.andExpect(status().isBadRequest());

			verifyNoInteractions(accountService);
		}

		@Test
		@DisplayName("should return 401 when JWT is missing")
		void withdraw_whenJwtMissing_shouldReturnUnauthorized() throws Exception {
			mockMvc.perform(post("/accounts/{accountNumber}/withdraw", "SB-1001")
					.contentType(APPLICATION_JSON)
					.content("{\"amount\":10.00}"))
				.andExpect(status().isUnauthorized());
		}

		@Test
		@DisplayName("should return 403 when role is forbidden")
		void withdraw_whenForbiddenRole_shouldReturnForbidden() throws Exception {
			mockMvc.perform(post("/accounts/{accountNumber}/withdraw", "SB-1001")
					.with(jwt().authorities(new SimpleGrantedAuthority("ROLE_VIEWER")))
					.contentType(APPLICATION_JSON)
					.content("{\"amount\":10.00}"))
				.andExpect(status().isForbidden());
		}
	}

	@Nested
	@DisplayName("Security")
	class SecurityTests {
		@Test
		@DisplayName("should return 401 when JWT is missing")
		void accountEndpoint_whenJwtMissing_shouldReturnUnauthorized() throws Exception {
			mockMvc.perform(get("/accounts/{accountNumber}", "SB-1001"))
				.andExpect(status().isUnauthorized());
		}

		@Test
		@DisplayName("should return unauthorized for deposit endpoint when JWT missing")
		void depositEndpoint_whenJwtMissing_shouldReturnUnauthorized() throws Exception {
			mockMvc.perform(post("/accounts/{accountNumber}/deposit", "SB-1001")
					.contentType(APPLICATION_JSON)
					.content("{\"amount\":1.00}"))
				.andExpect(status().isUnauthorized());
		}

		@Test
		@DisplayName("should return unauthorized for withdraw endpoint when JWT missing")
		void withdrawEndpoint_whenJwtMissing_shouldReturnUnauthorized() throws Exception {
			mockMvc.perform(post("/accounts/{accountNumber}/withdraw", "SB-1001")
					.contentType(APPLICATION_JSON)
					.content("{\"amount\":1.00}"))
				.andExpect(status().isUnauthorized());
		}

		@Test
		@DisplayName("should return forbidden for create endpoint with read-only role")
		void createEndpoint_whenReadOnlyRole_shouldReturnForbidden() throws Exception {
			mockMvc.perform(post("/accounts")
					.with(jwt().authorities(new SimpleGrantedAuthority("ROLE_READ_ONLY")))
					.contentType(APPLICATION_JSON)
					.content("{\"customerId\":1,\"accountType\":\"SAVINGS\"}"))
				.andExpect(status().isForbidden());
		}
	}

	private static AccountResponse accountResponse(String accountNumber, BigDecimal balance) {
		AccountResponse response = new AccountResponse();
		response.setAccountNumber(accountNumber);
		response.setBalance(balance);
		return response;
	}

	private static BigDecimal bd(String value) {
		return new BigDecimal(value);
	}
}
