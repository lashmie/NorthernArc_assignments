package org.northernarc.week5_assess.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.northernarc.week5_assess.dto.AuthRequest;
import org.northernarc.week5_assess.dto.AuthResponse;
import org.northernarc.week5_assess.dto.RegisterRequest;
import org.northernarc.week5_assess.exception.DuplicateResourceException;
import org.northernarc.week5_assess.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * AuthControllerTest - Comprehensive MockMvc Tests for Auth Controller
 *
 * TESTING COVERAGE:
 * ================
 * 1. REGISTER ENDPOINT (POST /auth/register):
 *    - Happy Path: Valid request -> 201 CREATED + JWT token
 *      Verifies: verify(authService).register() + verifyNoMoreInteractions()
 *    - Duplicate Email: 409 CONFLICT
 *      Verifies: verify(authService).register() was called
 *    - Validation Failures -> 400 BAD_REQUEST with verifyNoInteractions(authService)
 *      * Blank name
 *      * Invalid email format
 *      * Blank password
 *      * Password too short
 *      * Missing mandatory fields
 *      * Malformed JSON
 *      * Whitespace-only name
 *      * Null body literal ("null")
 *    - Media Type: 415 UNSUPPORTED_MEDIA_TYPE
 *    - Edge Cases: Mixed-case email acceptance when valid
 *
 * 2. LOGIN ENDPOINT (POST /auth/login):
 *    - Happy Path: Valid credentials -> 200 OK + JWT token
 *      Verifies: verify(authService).login() + verifyNoMoreInteractions()
 *    - Invalid Credentials: 401 UNAUTHORIZED
 *      Verifies: verify(authService).login() was called
 *    - Validation Failures -> 400 BAD_REQUEST with verifyNoInteractions(authService)
 *      * Blank email
 *      * Invalid email format
 *      * Blank password
 *      * Missing fields
 *      * Null body literal
 *      * Malformed JSON
 *      * Trailing comma JSON
 *    - Media Type: 415 UNSUPPORTED_MEDIA_TYPE
 *    - Edge Cases: Mixed-case email acceptance when valid
 *
 * KEY VERIFICATION PATTERNS:
 * =========================
 * - verify(authService).register/login(request) -> Confirms service called exactly once on success
 * - verifyNoMoreInteractions(authService) -> No other service methods called after success
 * - verifyNoInteractions(authService) -> Service NEVER called for validation failures
 *   This is CRITICAL: Bad requests must fail at validation layer BEFORE reaching service.
 *
 * BAD REQUEST STRATEGY:
 * ===================
 * Any request with missing/invalid values must:
 * 1. Return 400 BAD_REQUEST
 * 2. NOT invoke the service layer (verifyNoInteractions)
 * This ensures validation happens at controller/JSON binding layer, not at service.
 */
@WebMvcTest(controllers = AuthController.class)
@DisplayName("AuthController - MockMvc Tests")
public class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private AuthService authService;


	// Setup method called before each test method
	@BeforeEach
	void beforeEach() {
		// Reset mock state and clear any previous interactions with authService
		reset(authService);
	}

	// Cleanup method called after each test method
	@AfterEach
	void afterEach() {
		// Verify no unexpected interactions with mocks after each test
		verifyNoMoreInteractions(authService);
	}

	@Nested
	@DisplayName("POST /auth/register")
	class RegisterTests {

		// Test: Valid registration payload returns 201 CREATED with token
		@Test
		@DisplayName("should return 201 when register payload is valid")
		void register_whenValidRequest_shouldReturnCreated() throws Exception {
			RegisterRequest request = new RegisterRequest();
			request.setName("Asha Raman");
			request.setEmail("asha@northernarc.org");
			request.setPassword("StrongPass@123");

			AuthResponse response = new AuthResponse();
			response.setToken("jwt-token");
			when(authService.register(request)).thenReturn(response);

			mockMvc.perform(post("/auth/register")
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.token").value("jwt-token"));

			verify(authService, times(1)).register(request);
			verifyNoMoreInteractions(authService);
		}

		// Test: Duplicate email throws exception returns 409 CONFLICT
		@Test
		@DisplayName("should return 409 when email already exists")
		void register_whenDuplicateEmail_shouldReturnConflict() throws Exception {
			RegisterRequest request = new RegisterRequest();
			request.setName("Asha Raman");
			request.setEmail("asha@northernarc.org");
			request.setPassword("StrongPass@123");
			when(authService.register(request)).thenThrow(new DuplicateResourceException("Duplicate email"));

			mockMvc.perform(post("/auth/register")
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isConflict());

			verify(authService, times(1)).register(request);
		}

		// Test: Blank name returns 400 BAD_REQUEST
		@Test
		@DisplayName("should return 400 when name is blank")
		void register_whenNameBlank_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/auth/register")
					.contentType(APPLICATION_JSON)
					.content("{\"name\":\"\",\"email\":\"asha@northernarc.org\",\"password\":\"StrongPass@123\"}"))
				.andExpect(status().isBadRequest());

			verify(authService, times(0)).register(null);
		}

		// Test: Invalid email format returns 400 BAD_REQUEST
		@Test
		@DisplayName("should return 400 when email is invalid")
		void register_whenEmailInvalid_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/auth/register")
					.contentType(APPLICATION_JSON)
					.content("{\"name\":\"Asha\",\"email\":\"bad-email\",\"password\":\"StrongPass@123\"}"))
				.andExpect(status().isBadRequest());

			verify(authService, times(0)).register(null);
		}

		// Test: Blank password returns 400 BAD_REQUEST
		@Test
		@DisplayName("should return 400 when password is blank")
		void register_whenPasswordBlank_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/auth/register")
					.contentType(APPLICATION_JSON)
					.content("{\"name\":\"Asha\",\"email\":\"asha@northernarc.org\",\"password\":\"\"}"))
				.andExpect(status().isBadRequest());

			verify(authService, times(0)).register(null);
		}

		// Test: Short password returns 400 BAD_REQUEST
		@Test
		@DisplayName("should return 400 when password too short")
		void register_whenPasswordShort_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/auth/register")
					.contentType(APPLICATION_JSON)
					.content("{\"name\":\"Asha\",\"email\":\"asha@northernarc.org\",\"password\":\"123\"}"))
				.andExpect(status().isBadRequest());

			verify(authService, times(0)).register(null);
		}

		// Test: Missing mandatory fields returns 400 BAD_REQUEST
		@Test
		@DisplayName("should return 400 when mandatory fields missing")
		void register_whenFieldsMissing_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/auth/register")
					.contentType(APPLICATION_JSON)
					.content("{}"))
				.andExpect(status().isBadRequest());

			verify(authService, times(0)).register(null);
		}

		// Test: Malformed JSON returns 400 BAD_REQUEST
		@Test
		@DisplayName("should return 400 for malformed register JSON")
		void register_whenMalformedJson_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/auth/register")
					.contentType(APPLICATION_JSON)
					.content("{\"name\":"))
				.andExpect(status().isBadRequest());

			verify(authService, times(0)).register(null);
		}

		// Test: Missing Content-Type returns 415 UNSUPPORTED_MEDIA_TYPE
		@Test
		@DisplayName("should return 415 when register content type missing")
		void register_whenContentTypeMissing_shouldReturnUnsupportedMediaType() throws Exception {
			mockMvc.perform(post("/auth/register")
					.content("{\"name\":\"Asha\",\"email\":\"asha@northernarc.org\",\"password\":\"StrongPass@123\"}"))
				.andExpect(status().isUnsupportedMediaType());

			verify(authService, times(0)).register(null);
		}

		// Test: Unexpected error returns 500 INTERNAL_SERVER_ERROR
		@Test
		@DisplayName("should return 500 when register has unexpected error")
		void register_whenUnexpectedError_shouldReturnInternalServerError() throws Exception {
			RegisterRequest request = new RegisterRequest();
			request.setName("Asha Raman");
			request.setEmail("asha@northernarc.org");
			request.setPassword("StrongPass@123");
			when(authService.register(request)).thenThrow(new RuntimeException("unexpected"));

			mockMvc.perform(post("/auth/register")
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isInternalServerError());

			verify(authService, times(1)).register(request);
		}

		// Test: Whitespace-only name returns 400 BAD_REQUEST
		@Test
		@DisplayName("should trim-proof by rejecting whitespace-only name")
		void register_whenNameWhitespaceOnly_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/auth/register")
					.contentType(APPLICATION_JSON)
					.content("{\"name\":\"   \",\"email\":\"asha@northernarc.org\",\"password\":\"StrongPass@123\"}"))
				.andExpect(status().isBadRequest());

			verify(authService, times(0)).register(null);
		}

		// Test: Mixed-case email is accepted when valid returns 201 CREATED
		@Test
		@DisplayName("should accept register payload with mixed-case email when valid")
		void register_whenEmailMixedCase_shouldReturnCreated() throws Exception {
			RegisterRequest request = new RegisterRequest();
			request.setName("Asha Raman");
			request.setEmail("Asha@NorthernArc.Org");
			request.setPassword("StrongPass@123");

			AuthResponse response = new AuthResponse();
			response.setToken("jwt-token");
			when(authService.register(request)).thenReturn(response);

			mockMvc.perform(post("/auth/register")
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.token").value("jwt-token"));

			verify(authService, times(1)).register(request);
		}

		// Test: Null body literal returns 400 BAD_REQUEST
		@Test
		@DisplayName("should return bad request for null-like register body")
		void register_whenNullBodyLiteral_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/auth/register")
					.contentType(APPLICATION_JSON)
					.content("null"))
				.andExpect(status().isBadRequest());

			verify(authService, times(0)).register(null);
		}
	}

	@Nested
	@DisplayName("POST /auth/login")
	class LoginTests {

		// Test: Valid credentials returns 200 OK with JWT token
		@Test
		@DisplayName("should return 200 with JWT token when credentials are valid")
		void login_whenValidCredentials_shouldReturnOk() throws Exception {
			AuthRequest request = new AuthRequest();
			request.setEmail("asha@northernarc.org");
			request.setPassword("StrongPass@123");

			AuthResponse response = new AuthResponse();
			response.setToken("jwt-token");
			when(authService.login(request)).thenReturn(response);

			mockMvc.perform(post("/auth/login")
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").value("jwt-token"));

			verify(authService, times(1)).login(request);
			verifyNoMoreInteractions(authService);
		}

		// Test: Invalid credentials returns 401 UNAUTHORIZED
		@Test
		@DisplayName("should return 401 for invalid credentials")
		void login_whenInvalidCredentials_shouldReturnUnauthorized() throws Exception {
			AuthRequest request = new AuthRequest();
			request.setEmail("asha@northernarc.org");
			request.setPassword("WrongPass");

			when(authService.login(request)).thenThrow(new BadCredentialsException("Bad credentials"));

			mockMvc.perform(post("/auth/login")
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isUnauthorized());

			verify(authService, times(1)).login(request);
		}

		// Test: Malformed JSON returns 400 BAD_REQUEST
		@Test
		@DisplayName("should return 400 for malformed JSON")
		void login_whenMalformedJson_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/auth/login")
					.contentType(APPLICATION_JSON)
					.content("{\"email\":"))
				.andExpect(status().isBadRequest());

			verify(authService, times(0)).login(null);
		}

		// Test: Blank email returns 400 BAD_REQUEST
		@Test
		@DisplayName("should return 400 when login email is blank")
		void login_whenEmailBlank_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/auth/login")
					.contentType(APPLICATION_JSON)
					.content("{\"email\":\"\",\"password\":\"StrongPass@123\"}"))
				.andExpect(status().isBadRequest());

			verify(authService, times(0)).login(null);
		}

		// Test: Invalid email format returns 400 BAD_REQUEST
		@Test
		@DisplayName("should return 400 when login email invalid format")
		void login_whenEmailInvalid_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/auth/login")
					.contentType(APPLICATION_JSON)
					.content("{\"email\":\"bad\",\"password\":\"StrongPass@123\"}"))
				.andExpect(status().isBadRequest());

			verify(authService, times(0)).login(null);
		}

		// Test: Blank password returns 400 BAD_REQUEST
		@Test
		@DisplayName("should return 400 when login password blank")
		void login_whenPasswordBlank_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/auth/login")
					.contentType(APPLICATION_JSON)
					.content("{\"email\":\"asha@northernarc.org\",\"password\":\"\"}"))
				.andExpect(status().isBadRequest());

			verify(authService, times(0)).login(null);
		}

		// Test: Missing required fields returns 400 BAD_REQUEST
		@Test
		@DisplayName("should return 400 when login payload missing fields")
		void login_whenFieldsMissing_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/auth/login")
					.contentType(APPLICATION_JSON)
					.content("{}"))
				.andExpect(status().isBadRequest());

			verify(authService, times(0)).login(null);
		}

		// Test: Null literal body returns 400 BAD_REQUEST
		@Test
		@DisplayName("should return 400 when login body is null literal")
		void login_whenNullLiteralBody_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/auth/login")
					.contentType(APPLICATION_JSON)
					.content("null"))
				.andExpect(status().isBadRequest());

			verify(authService, times(0)).login(null);
		}

		// Test: Missing Content-Type returns 415 UNSUPPORTED_MEDIA_TYPE
		@Test
		@DisplayName("should return 415 when login content type missing")
		void login_whenContentTypeMissing_shouldReturnUnsupportedMediaType() throws Exception {
			mockMvc.perform(post("/auth/login")
					.content("{\"email\":\"asha@northernarc.org\",\"password\":\"StrongPass@123\"}"))
				.andExpect(status().isUnsupportedMediaType());

			verify(authService, times(0)).login(null);
		}

		// Test: Unexpected error returns 500 INTERNAL_SERVER_ERROR
		@Test
		@DisplayName("should return 500 when login has unexpected service error")
		void login_whenUnexpectedError_shouldReturnInternalServerError() throws Exception {
			AuthRequest request = new AuthRequest();
			request.setEmail("asha@northernarc.org");
			request.setPassword("StrongPass@123");
			when(authService.login(request)).thenThrow(new RuntimeException("unexpected"));

			mockMvc.perform(post("/auth/login")
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isInternalServerError());

			verify(authService, times(1)).login(request);
		}

		// Test: Mixed-case valid email is accepted returns 200 OK
		@Test
		@DisplayName("should return 200 for mixed-case valid email on login")
		void login_whenEmailMixedCase_shouldReturnOk() throws Exception {
			AuthRequest request = new AuthRequest();
			request.setEmail("Asha@NorthernArc.Org");
			request.setPassword("StrongPass@123");

			AuthResponse response = new AuthResponse();
			response.setToken("jwt-mixed");
			when(authService.login(request)).thenReturn(response);

			mockMvc.perform(post("/auth/login")
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").value("jwt-mixed"));

			verify(authService, times(1)).login(request);
		}

		// Test: Malformed JSON with trailing comma returns 400 BAD_REQUEST
		@Test
		@DisplayName("should return 400 when payload contains invalid json trailing comma")
		void login_whenTrailingCommaJson_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/auth/login")
					.contentType(APPLICATION_JSON)
					.content("{\"email\":\"asha@northernarc.org\",\"password\":\"StrongPass@123\",}"))
				.andExpect(status().isBadRequest());

			verify(authService, times(0)).login(null);
		}
	}
}

