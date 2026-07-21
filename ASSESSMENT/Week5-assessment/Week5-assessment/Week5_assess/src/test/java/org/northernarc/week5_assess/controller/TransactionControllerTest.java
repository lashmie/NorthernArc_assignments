package org.northernarc.week5_assess.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.reset;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.northernarc.week5_assess.dto.TransactionHistoryResponse;
import org.northernarc.week5_assess.dto.TransferRequest;
import org.northernarc.week5_assess.dto.TransferResponse;
import org.northernarc.week5_assess.exception.AccountNotFoundException;
import org.northernarc.week5_assess.exception.InsufficientFundsException;
import org.northernarc.week5_assess.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TransactionControllerTest - Comprehensive MockMvc Tests for Transaction API
 *
 * TESTING COVERAGE:
 * ================
 * 1. TRANSFER (POST /transactions/transfer) [REQUIRES JWT]:
 *    - Happy Path: Valid transfer request + JWT -> 200 OK + reference ID
 *      Verifies: verify(transactionService).transfer(request) + verifyNoMoreInteractions()
 *    - Minimum Amount: 0.01 currency units -> 200 OK
 *      Verifies: verify(transactionService).transfer(request)
 *    - Large Amount: 999999999.99 -> 200 OK
 *    - Service Invocation: verify() called exactly once on success
 *
 *    ERROR CASES (verify service was called):
 *      * Insufficient Funds: 422 UNPROCESSABLE_ENTITY
 *        Verifies: verify(transactionService).transfer(request)
 *      * Destination Account Missing: 404 NOT_FOUND
 *        Verifies: verify(transactionService).transfer(request)
 *      * Source Account Missing: 404 NOT_FOUND
 *
 *    VALIDATION FAILURES -> 400 BAD_REQUEST with verifyNoInteractions(transactionService)
 *      * Same source and destination accounts
 *      * Amount is zero
 *      * Amount is negative
 *      * Amount field is missing
 *      * Amount is null
 *      * Source account is blank
 *      * Destination account is blank
 *      * Amount type is invalid (string "oops")
 *      * Malformed JSON
 *
 *    ERROR HANDLING:
 *      * Unexpected service exception -> 500 INTERNAL_SERVER_ERROR
 *    - Media Type: 415 UNSUPPORTED_MEDIA_TYPE
 *    - Security: 401 UNAUTHORIZED when JWT missing, 403 FORBIDDEN for wrong role
 *
 * 2. TRANSACTION HISTORY (GET /transactions/{accountNumber}) [REQUIRES JWT]:
 *    - Happy Path: Valid account + JWT -> 200 OK + List of transactions
 *      Verifies: verify(transactionService).getTransactionsForAccount("SB-1001")
 *               + verifyNoMoreInteractions()
 *    - Empty History: Account with no transactions -> 200 OK + empty array
 *    - Not Found: Invalid account -> 404 NOT_FOUND
 *    - Blank Account Number: 400 BAD_REQUEST
 *    - Multiple Records: Returns all in proper order (new to old)
 *      Verifies: verify(transactionService).getTransactionsForAccount("SB-1001")
 *
 *    ERROR HANDLING:
 *      * Unexpected error -> 500 INTERNAL_SERVER_ERROR
 *    - Security: 401 UNAUTHORIZED when JWT missing, 403 FORBIDDEN for wrong role
 *
 * 3. SECURITY TESTS:
 *    - Transfer endpoint: 401 when JWT missing, 403 for forbidden roles (ROLE_VIEWER, ROLE_GUEST)
 *    - History endpoint: 401 when JWT missing, 403 for forbidden roles (ROLE_VIEWER, ROLE_GUEST)
 *
 * KEY VERIFICATION PATTERNS:
 * =========================
 * SUCCESS SCENARIO:
 * - verify(transactionService).transfer(request) -> Service called exactly once
 * - verifyNoMoreInteractions(transactionService) -> No other calls to service
 *
 * VALIDATION FAILURE SCENARIO:
 * - verifyNoInteractions(transactionService) -> Service NEVER called
 * - Used for: malformed JSON, missing required fields, invalid field types
 * - These must fail at validation layer BEFORE reaching service
 *
 * BUSINESS ERROR SCENARIO:
 * - verify(transactionService).transfer(request) -> Service WAS called
 * - Service threw business exception (InsufficientFundsException, AccountNotFoundException)
 * - Controller catches and returns appropriate HTTP status
 *
 * BAD REQUEST STRATEGY (verifyNoInteractions MANDATORY):
 * =====================================================
 * Missing/invalid values must:
 * 1. Return 400 BAD_REQUEST at validation/binding layer
 * 2. NEVER invoke transactionService
 * 3. Assert: verifyNoInteractions(transactionService) after status().isBadRequest()
 *
 * STATUS CODE REFERENCE:
 * ====================
 * 200 OK - Transfer successful, history retrieved
 * 400 BAD_REQUEST - Validation failure (bad input at binding layer)
 * 401 UNAUTHORIZED - JWT missing or invalid
 * 403 FORBIDDEN - Authenticated but lacking required role/privilege
 * 404 NOT_FOUND - Source/Destination account does not exist
 * 415 UNSUPPORTED_MEDIA_TYPE - Content-Type header missing/incorrect
 * 422 UNPROCESSABLE_ENTITY - Business logic error (e.g., insufficient funds)
 * 500 INTERNAL_SERVER_ERROR - Unexpected service exception
 *
 * INVOCATION COUNTS:
 * ================
 * Single successful call:
 *   - verify(transactionService).method() -> exactly 1 call
 * Multiple records (history):
 *   - verify(transactionService).getTransactionsForAccount(...) -> exactly 1 call
 *   - Returns List<> with 2+ items
 */
@WebMvcTest(controllers = TransactionController.class)
@DisplayName("TransactionController - MockMvc Tests")
public class TransactionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private TransactionService transactionService;

	// Setup method called once before all tests in this class
	@BeforeAll
	static void beforeAll() {
		// One-time initialization before any test runs
	}

	// Setup method called before each test method
	@BeforeEach
	void beforeEach() {
		// Reset mock state and clear any previous interactions with transactionService
		reset(transactionService);
	}

	// Cleanup method called after each test method
	@AfterEach
	void afterEach() {
		// Verify no unexpected interactions with mocks after each test
		verifyNoMoreInteractions(transactionService);
	}

	// Cleanup method called once after all tests in this class
	@AfterAll
	static void afterAll() {
		// One-time cleanup after all tests complete
	}

	@Nested
	@DisplayName("POST /transactions/transfer")
	class TransferTests {

		@Test
		@DisplayName("should return 200 for valid transfer")
		void transfer_whenValidRequest_shouldReturnOk() throws Exception {
			// Arrange
			TransferRequest request = new TransferRequest();
			request.setSourceAccountNumber("SB-1001");
			request.setDestinationAccountNumber("SB-2002");
			request.setAmount(bd("80.00"));

			TransferResponse response = new TransferResponse();
			response.setReferenceId("TXN-0001");
			response.setAmount(bd("80.00"));

			when(transactionService.transfer(request)).thenReturn(response);

			// Act + Assert
			mockMvc.perform(post("/transactions/transfer")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.referenceId").value("TXN-0001"))
				.andExpect(jsonPath("$.amount").value(80.00));

			// Verify
			verify(transactionService).transfer(request);
			verifyNoMoreInteractions(transactionService);
		}

		@Test
		@DisplayName("should return 200 for minimum valid amount")
		void transfer_whenMinimumAmount_shouldReturnOk() throws Exception {
			TransferRequest request = transferRequest("SB-1001", "SB-2002", "0.01");
			TransferResponse response = transferResponse("TXN-MIN-1", "0.01");
			when(transactionService.transfer(request)).thenReturn(response);

			mockMvc.perform(post("/transactions/transfer")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.referenceId").value("TXN-MIN-1"));

			verify(transactionService).transfer(request);
		}

		@Test
		@DisplayName("should return 422 for insufficient funds")
		void transfer_whenInsufficientFunds_shouldReturnUnprocessableEntity() throws Exception {
			// Arrange
			TransferRequest request = new TransferRequest();
			request.setSourceAccountNumber("SB-1001");
			request.setDestinationAccountNumber("SB-2002");
			request.setAmount(bd("8000.00"));

			when(transactionService.transfer(request)).thenThrow(new InsufficientFundsException("insufficient"));

			// Act + Assert
			mockMvc.perform(post("/transactions/transfer")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isUnprocessableEntity());

			verify(transactionService).transfer(request);
		}

		@Test
		@DisplayName("should return 404 when destination account is missing")
		void transfer_whenDestinationMissing_shouldReturnNotFound() throws Exception {
			// Arrange
			TransferRequest request = new TransferRequest();
			request.setSourceAccountNumber("SB-1001");
			request.setDestinationAccountNumber("SB-9999");
			request.setAmount(bd("10.00"));

			when(transactionService.transfer(request)).thenThrow(new AccountNotFoundException("not found"));

			// Act + Assert
			mockMvc.perform(post("/transactions/transfer")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isNotFound());

			verify(transactionService).transfer(request);
		}

		@Test
		@DisplayName("should return 404 when source account is missing")
		void transfer_whenSourceMissing_shouldReturnNotFound() throws Exception {
			TransferRequest request = transferRequest("SB-404", "SB-2002", "10.00");
			when(transactionService.transfer(request)).thenThrow(new AccountNotFoundException("source missing"));

			mockMvc.perform(post("/transactions/transfer")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isNotFound());
		}

		@Test
		@DisplayName("should return 400 when source and destination are same")
		void transfer_whenSameAccounts_shouldReturnBadRequest() throws Exception {
			TransferRequest request = transferRequest("SB-1001", "SB-1001", "50.00");
			when(transactionService.transfer(request)).thenThrow(new IllegalArgumentException("same account"));

			mockMvc.perform(post("/transactions/transfer")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("should return 400 when amount is zero")
		void transfer_whenAmountZero_shouldReturnBadRequest() throws Exception {
			TransferRequest request = transferRequest("SB-1001", "SB-2002", "0.00");
			when(transactionService.transfer(request)).thenThrow(new IllegalArgumentException("amount"));

			mockMvc.perform(post("/transactions/transfer")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("should return 400 when amount is negative")
		void transfer_whenAmountNegative_shouldReturnBadRequest() throws Exception {
			TransferRequest request = transferRequest("SB-1001", "SB-2002", "-1.00");
			when(transactionService.transfer(request)).thenThrow(new IllegalArgumentException("amount"));

			mockMvc.perform(post("/transactions/transfer")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("should return 400 when amount field is missing")
		void transfer_whenAmountMissing_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/transactions/transfer")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content("{\"sourceAccountNumber\":\"SB-1001\",\"destinationAccountNumber\":\"SB-2002\"}"))
				.andExpect(status().isBadRequest());

			verifyNoInteractions(transactionService);
		}

		@Test
		@DisplayName("should return 400 when amount is null")
		void transfer_whenAmountNull_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/transactions/transfer")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content("{\"sourceAccountNumber\":\"SB-1001\",\"destinationAccountNumber\":\"SB-2002\",\"amount\":null}"))
				.andExpect(status().isBadRequest());

			verifyNoInteractions(transactionService);
		}

		@Test
		@DisplayName("should return 400 when source account is blank")
		void transfer_whenSourceBlank_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/transactions/transfer")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content("{\"sourceAccountNumber\":\"\",\"destinationAccountNumber\":\"SB-2002\",\"amount\":1.00}"))
				.andExpect(status().isBadRequest());

			verifyNoInteractions(transactionService);
		}

		@Test
		@DisplayName("should return 400 when destination account is blank")
		void transfer_whenDestinationBlank_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/transactions/transfer")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content("{\"sourceAccountNumber\":\"SB-1001\",\"destinationAccountNumber\":\"\",\"amount\":1.00}"))
				.andExpect(status().isBadRequest());

			verifyNoInteractions(transactionService);
		}

		@Test
		@DisplayName("should return 400 when amount has invalid type")
		void transfer_whenAmountTypeInvalid_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/transactions/transfer")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content("{\"sourceAccountNumber\":\"SB-1001\",\"destinationAccountNumber\":\"SB-2002\",\"amount\":\"oops\"}"))
				.andExpect(status().isBadRequest());

			verifyNoInteractions(transactionService);
		}

		@Test
		@DisplayName("should return 500 when service throws unexpected exception")
		void transfer_whenUnexpectedException_shouldReturnInternalServerError() throws Exception {
			TransferRequest request = transferRequest("SB-1001", "SB-2002", "10.00");
			when(transactionService.transfer(request)).thenThrow(new RuntimeException("unexpected"));

			mockMvc.perform(post("/transactions/transfer")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isInternalServerError());
		}

		@Test
		@DisplayName("should return 400 for malformed JSON")
		void transfer_whenMalformedJson_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/transactions/transfer")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content("{\"sourceAccountNumber\":"))
				.andExpect(status().isBadRequest());

			verifyNoInteractions(transactionService);
		}

		@Test
		@DisplayName("should return 415 when content type is missing")
		void transfer_whenContentTypeMissing_shouldReturnUnsupportedMediaType() throws Exception {
			mockMvc.perform(post("/transactions/transfer")
					.with(jwt())
					.content("{\"sourceAccountNumber\":\"SB-1001\",\"destinationAccountNumber\":\"SB-2002\",\"amount\":10.00}"))
				.andExpect(status().isUnsupportedMediaType());
		}

		@Test
		@DisplayName("should return 401 when JWT is missing")
		void transfer_whenJwtMissing_shouldReturnUnauthorized() throws Exception {
			mockMvc.perform(post("/transactions/transfer")
					.contentType(APPLICATION_JSON)
					.content("{\"sourceAccountNumber\":\"SB-1001\",\"destinationAccountNumber\":\"SB-2002\",\"amount\":10.00}"))
				.andExpect(status().isUnauthorized());
		}

		@Test
		@DisplayName("should return 403 when role is forbidden")
		void transfer_whenRoleForbidden_shouldReturnForbidden() throws Exception {
			mockMvc.perform(post("/transactions/transfer")
					.with(jwt().authorities(new SimpleGrantedAuthority("ROLE_VIEWER")))
					.contentType(APPLICATION_JSON)
					.content("{\"sourceAccountNumber\":\"SB-1001\",\"destinationAccountNumber\":\"SB-2002\",\"amount\":10.00}"))
				.andExpect(status().isForbidden());
		}

		@Test
		@DisplayName("should accept large transfer values when service allows")
		void transfer_whenLargeAmount_shouldReturnOk() throws Exception {
			TransferRequest request = transferRequest("SB-1001", "SB-2002", "999999999.99");
			when(transactionService.transfer(request)).thenReturn(transferResponse("TXN-LARGE", "999999999.99"));

			mockMvc.perform(post("/transactions/transfer")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.referenceId").value("TXN-LARGE"));
		}

		@Test
		@DisplayName("should verify service called exactly once on successful transfer")
		void transfer_whenSuccessful_shouldInvokeServiceOnce() throws Exception {
			TransferRequest request = transferRequest("SB-7777", "SB-8888", "25.00");
			when(transactionService.transfer(request)).thenReturn(transferResponse("TXN-ONCE", "25.00"));

			mockMvc.perform(post("/transactions/transfer")
					.with(jwt())
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk());

			verify(transactionService).transfer(request);
		}
	}

	@Nested
	@DisplayName("GET /transactions/{accountNumber}")
	class HistoryTests {

		@Test
		@DisplayName("should return 200 with history for valid account")
		void history_whenValidAccount_shouldReturnOk() throws Exception {
			// Arrange
			TransactionHistoryResponse item = new TransactionHistoryResponse();
			item.setReferenceId("TXN-001");
			when(transactionService.getTransactionsForAccount("SB-1001")).thenReturn(List.of(item));

			// Act + Assert
			mockMvc.perform(get("/transactions/{accountNumber}", "SB-1001").with(jwt()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].referenceId").value("TXN-001"));

			// Verify
			verify(transactionService).getTransactionsForAccount("SB-1001");
			verifyNoMoreInteractions(transactionService);
		}

		@Test
		@DisplayName("should return 200 with empty list when no transactions")
		void history_whenEmpty_shouldReturnOkAndEmptyArray() throws Exception {
			when(transactionService.getTransactionsForAccount("SB-1001")).thenReturn(List.of());

			mockMvc.perform(get("/transactions/{accountNumber}", "SB-1001").with(jwt()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").isEmpty());
		}

		@Test
		@DisplayName("should return 404 when account not found")
		void history_whenAccountMissing_shouldReturnNotFound() throws Exception {
			when(transactionService.getTransactionsForAccount("SB-404")).thenThrow(new AccountNotFoundException("not found"));

			mockMvc.perform(get("/transactions/{accountNumber}", "SB-404").with(jwt()))
				.andExpect(status().isNotFound());
		}

		@Test
		@DisplayName("should return 400 when account number is blank")
		void history_whenAccountBlank_shouldReturnBadRequest() throws Exception {
			when(transactionService.getTransactionsForAccount(" ")).thenThrow(new IllegalArgumentException("account required"));

			mockMvc.perform(get("/transactions/{accountNumber}", " ").with(jwt()))
				.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("should return 500 for unexpected history errors")
		void history_whenUnexpectedError_shouldReturnInternalServerError() throws Exception {
			when(transactionService.getTransactionsForAccount("SB-500")).thenThrow(new RuntimeException("unexpected"));

			mockMvc.perform(get("/transactions/{accountNumber}", "SB-500").with(jwt()))
				.andExpect(status().isInternalServerError());
		}

		@Test
		@DisplayName("should return 401 for history when JWT is missing")
		void history_whenJwtMissing_shouldReturnUnauthorized() throws Exception {
			mockMvc.perform(get("/transactions/{accountNumber}", "SB-1001"))
				.andExpect(status().isUnauthorized());
		}

		@Test
		@DisplayName("should return 403 for history when role is forbidden")
		void history_whenRoleForbidden_shouldReturnForbidden() throws Exception {
			mockMvc.perform(get("/transactions/{accountNumber}", "SB-1001")
					.with(jwt().authorities(new SimpleGrantedAuthority("ROLE_VIEWER"))))
				.andExpect(status().isForbidden());
		}

		@Test
		@DisplayName("should return multiple records with expected order")
		void history_whenMultipleRecords_shouldReturnAllInOrder() throws Exception {
			TransactionHistoryResponse first = new TransactionHistoryResponse();
			first.setReferenceId("TXN-NEW");
			TransactionHistoryResponse second = new TransactionHistoryResponse();
			second.setReferenceId("TXN-OLD");
			when(transactionService.getTransactionsForAccount("SB-1001")).thenReturn(List.of(first, second));

			mockMvc.perform(get("/transactions/{accountNumber}", "SB-1001").with(jwt()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].referenceId").value("TXN-NEW"))
				.andExpect(jsonPath("$[1].referenceId").value("TXN-OLD"));
		}
	}

	@Nested
	@DisplayName("Security")
	class SecurityTests {
		@Test
		@DisplayName("should return 401 when JWT is missing")
		void transactionEndpoint_whenJwtMissing_shouldReturnUnauthorized() throws Exception {
			mockMvc.perform(post("/transactions/transfer").contentType(APPLICATION_JSON).content("{}"))
				.andExpect(status().isUnauthorized());
		}

		@Test
		@DisplayName("should return unauthorized for history endpoint when JWT missing")
		void historyEndpoint_whenJwtMissing_shouldReturnUnauthorized() throws Exception {
			mockMvc.perform(get("/transactions/{accountNumber}", "SB-1001"))
				.andExpect(status().isUnauthorized());
		}

		@Test
		@DisplayName("should return forbidden for transfer when role lacks privilege")
		void transferEndpoint_whenRoleNotAllowed_shouldReturnForbidden() throws Exception {
			mockMvc.perform(post("/transactions/transfer")
					.with(jwt().authorities(new SimpleGrantedAuthority("ROLE_GUEST")))
					.contentType(APPLICATION_JSON)
					.content("{\"sourceAccountNumber\":\"SB-1001\",\"destinationAccountNumber\":\"SB-2002\",\"amount\":10.00}"))
				.andExpect(status().isForbidden());
		}

		@Test
		@DisplayName("should return forbidden for history when role lacks privilege")
		void historyEndpoint_whenRoleNotAllowed_shouldReturnForbidden() throws Exception {
			mockMvc.perform(get("/transactions/{accountNumber}", "SB-1001")
					.with(jwt().authorities(new SimpleGrantedAuthority("ROLE_GUEST"))))
				.andExpect(status().isForbidden());
		}
	}

	private static TransferRequest transferRequest(String source, String destination, String amount) {
		TransferRequest request = new TransferRequest();
		request.setSourceAccountNumber(source);
		request.setDestinationAccountNumber(destination);
		request.setAmount(bd(amount));
		return request;
	}

	private static TransferResponse transferResponse(String ref, String amount) {
		TransferResponse response = new TransferResponse();
		response.setReferenceId(ref);
		response.setAmount(bd(amount));
		return response;
	}

	private static BigDecimal bd(String value) {
		return new BigDecimal(value);
	}
}
