package org.northernarc.week5_assess.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.northernarc.week5_assess.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * AuthenticationControllerTest - Comprehensive MockMvc Tests for Authentication API
 *
 * TESTING COVERAGE:
 * ================
 * 1. REGISTER ENDPOINT (POST /api/auth/register):
 *    - Happy Path: Valid registration -> 201 CREATED + customer response
 *    - Error Cases: Duplicate email (409), invalid email format (400)
 *    - Missing Fields: name, password, phoneNumber -> 400 BAD_REQUEST with verifyNoInteractions(service)
 *    - Validation: Phone length (10 digits), blank fields, null body, malformed JSON
 *    - Media Type: Unsupported media type -> 415 UNSUPPORTED_MEDIA_TYPE with verifyNoInteractions
 *
 * 2. LOGIN ENDPOINT (POST /api/auth/login):
 *    - Happy Path: Valid credentials -> 200 OK + JWT token
 *    - Error Cases: Invalid credentials (401), unknown email (401)
 *    - Missing Fields: email, password -> 400 BAD_REQUEST with verifyNoInteractions(service)
 *    - Validation: Blank credentials, null body, malformed JSON
 *    - Media Type: Unsupported media type -> 415 with verifyNoInteractions
 *
 * VERIFICATION PATTERNS:
 * ====================
 * - verify(authenticationService).register/login(...) -> Service called exactly once on success
 * - verifyNoInteractions(authenticationService) -> Service NEVER called for invalid requests
 *   (Tests with bad request status should NOT touch the service layer)
 *
 * BAD REQUEST BEHAVIOR:
 * ====================
 * Missing required fields MUST NOT invoke service, returning 400 immediately at validation layer.
 * The test verifies this by asserting verifyNoInteractions() after expecting isBadRequest().
 */
@WebMvcTest(AuthenticationController.class)
@DisplayName("AuthenticationControllerTest")
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    // Setup method called once before all tests in this class
    @BeforeAll
    static void beforeAll() {
        // One-time initialization before any test runs
    }

    // Setup method called before each test method
    @BeforeEach
    void beforeEach() {
        // Reset mock state and clear any previous interactions
        reset(authenticationService);
    }

    // Cleanup method called after each test method
    @AfterEach
    void afterEach() {
        // Verify no unexpected interactions with mocks after each test
        verifyNoMoreInteractions(authenticationService);
    }

    // Cleanup method called once after all tests in this class
    @AfterAll
    static void afterAll() {
        // One-time cleanup after all tests complete
    }

    @Nested
    @DisplayName("POST /api/auth/register")
    class RegisterTests {

        // Test: Valid registration with all required fields returns 201 CREATED
        @Test
        @DisplayName("should register customer successfully")
        void shouldRegisterCustomerSuccessfully() throws Exception {
            Map<String, Object> request = validRegisterRequest();
            Map<String, Object> response = Map.of(
                "id", 101L,
                "name", "Asha Raman",
                "email", "asha@northernarc.org",
                "phoneNumber", "9000000001"
            );
            org.mockito.Mockito.when(authenticationService.register(request))
                .thenReturn(response);

            mockMvc.perform(post("/api/auth/register")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(101L))
                .andExpect(jsonPath("$.email").value("asha@northernarc.org"));

            verify(authenticationService, org.mockito.Mockito.times(1)).register(request);
        }

        // Test: Duplicate email in register request returns 409 CONFLICT
        @Test
        @DisplayName("should return conflict when duplicate email")
        void shouldReturnConflictWhenDuplicateEmail() throws Exception {
            Map<String, Object> request = validRegisterRequest();
            org.mockito.Mockito.when(authenticationService.register(request))
                .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Duplicate email"));

            mockMvc.perform(post("/api/auth/register")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

            verify(authenticationService, org.mockito.Mockito.times(1)).register(request);
        }

        // Test: Invalid email format rejected at validation layer returns 400
        @Test
        @DisplayName("should return bad request for invalid email")
        void shouldReturnBadRequestForInvalidEmail() throws Exception {
            Map<String, Object> request = validRegisterRequest();
            request.put("email", "invalid");

            mockMvc.perform(post("/api/auth/register")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

            verify(authenticationService, org.mockito.Mockito.never()).register(request);
        }

        // Test: Missing name field returns 400 BAD_REQUEST
        @Test
        @DisplayName("should return bad request for missing customer name")
        void shouldReturnBadRequestForMissingName() throws Exception {
            Map<String, Object> request = validRegisterRequest();
            request.remove("name");

            mockMvc.perform(post("/api/auth/register")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

            verify(authenticationService, org.mockito.Mockito.never()).register(request);
        }

        // Test: Missing password field returns 400 BAD_REQUEST
        @Test
        @DisplayName("should return bad request for missing password")
        void shouldReturnBadRequestForMissingPassword() throws Exception {
            Map<String, Object> request = validRegisterRequest();
            request.remove("password");

            mockMvc.perform(post("/api/auth/register")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

            verify(authenticationService, org.mockito.Mockito.never()).register(request);
        }

        // Test: Missing phone number field returns 400 BAD_REQUEST
        @Test
        @DisplayName("should return bad request for missing phone number")
        void shouldReturnBadRequestForMissingPhoneNumber() throws Exception {
            Map<String, Object> request = validRegisterRequest();
            request.remove("phoneNumber");

            mockMvc.perform(post("/api/auth/register")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

            verify(authenticationService, org.mockito.Mockito.never()).register(request);
        }

        // Test: Phone number less than 10 digits returns 400 BAD_REQUEST
        @Test
        @DisplayName("should return bad request when phone less than 10 digits")
        void shouldReturnBadRequestWhenPhoneLessThan10() throws Exception {
            Map<String, Object> request = validRegisterRequest();
            request.put("phoneNumber", "12345");

            mockMvc.perform(post("/api/auth/register")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

            verify(authenticationService, org.mockito.Mockito.never()).register(request);
        }

        // Test: Phone number greater than 10 digits returns 400 BAD_REQUEST
        @Test
        @DisplayName("should return bad request when phone greater than 10 digits")
        void shouldReturnBadRequestWhenPhoneGreaterThan10() throws Exception {
            Map<String, Object> request = validRegisterRequest();
            request.put("phoneNumber", "123456789012");

            mockMvc.perform(post("/api/auth/register")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

            verify(authenticationService, org.mockito.Mockito.never()).register(request);
        }

        // Test: Blank fields (spaces only) returns 400 BAD_REQUEST
        @Test
        @DisplayName("should return bad request for blank request fields")
        void shouldReturnBadRequestForBlankFields() throws Exception {
            Map<String, Object> request = Map.of(
                "name", " ",
                "email", " ",
                "password", " ",
                "phoneNumber", " "
            );

            mockMvc.perform(post("/api/auth/register")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

            verify(authenticationService, org.mockito.Mockito.never()).register(request);
        }

        // Test: Null request body returns 400 BAD_REQUEST
        @Test
        @DisplayName("should return bad request for null request body")
        void shouldReturnBadRequestForNullRequestBody() throws Exception {
            mockMvc.perform(post("/api/auth/register")
                    .contentType(APPLICATION_JSON)
                    .content("null"))
                .andExpect(status().isBadRequest());

            verify(authenticationService, org.mockito.Mockito.never()).register(null);
        }

        // Test: Malformed JSON returns 400 BAD_REQUEST
        @Test
        @DisplayName("should return bad request for malformed json")
        void shouldReturnBadRequestForMalformedJson() throws Exception {
            mockMvc.perform(post("/api/auth/register")
                    .contentType(APPLICATION_JSON)
                    .content("{\"name\":"))
                .andExpect(status().isBadRequest());

            verify(authenticationService, org.mockito.Mockito.never()).register(null);
        }

        // Test: Missing Content-Type header returns 415 UNSUPPORTED_MEDIA_TYPE
        @Test
        @DisplayName("should return unsupported media type")
        void shouldReturnUnsupportedMediaType() throws Exception {
            mockMvc.perform(post("/api/auth/register")
                    .content("{}"))
                .andExpect(status().isUnsupportedMediaType());

            verify(authenticationService, org.mockito.Mockito.never()).register(null);
        }
    }

    @Nested
    @DisplayName("POST /api/auth/login")
    class LoginTests {

        // Test: Valid login credentials returns 200 OK with JWT token
        @Test
        @DisplayName("should login successfully")
        void shouldLoginSuccessfully() throws Exception {
            Map<String, Object> request = validLoginRequest();
            org.mockito.Mockito.when(authenticationService.login(request))
                .thenReturn(Map.of("token", "jwt-token-value"));

            mockMvc.perform(post("/api/auth/login")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value("jwt-token-value"));

            verify(authenticationService, org.mockito.Mockito.times(1)).login(request);
        }

        // Test: Invalid credentials returns 401 UNAUTHORIZED
        @Test
        @DisplayName("should return unauthorized for invalid credentials")
        void shouldReturnUnauthorizedForInvalidCredentials() throws Exception {
            Map<String, Object> request = validLoginRequest();
            org.mockito.Mockito.when(authenticationService.login(request))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

            mockMvc.perform(post("/api/auth/login")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

            verify(authenticationService, org.mockito.Mockito.times(1)).login(request);
        }

        // Test: Unknown email returns 401 UNAUTHORIZED
        @Test
        @DisplayName("should return unauthorized for unknown email")
        void shouldReturnUnauthorizedForUnknownEmail() throws Exception {
            Map<String, Object> request = validLoginRequest();
            org.mockito.Mockito.when(authenticationService.login(request))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unknown email"));

            mockMvc.perform(post("/api/auth/login")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

            verify(authenticationService, org.mockito.Mockito.times(1)).login(request);
        }

        // Test: Missing email field returns 400 BAD_REQUEST
        @Test
        @DisplayName("should return bad request for missing email")
        void shouldReturnBadRequestForMissingEmail() throws Exception {
            Map<String, Object> request = validLoginRequest();
            request.remove("email");

            mockMvc.perform(post("/api/auth/login")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

            verify(authenticationService, org.mockito.Mockito.never()).login(request);
        }

        // Test: Missing password field returns 400 BAD_REQUEST
        @Test
        @DisplayName("should return bad request for missing password")
        void shouldReturnBadRequestForMissingPassword() throws Exception {
            Map<String, Object> request = validLoginRequest();
            request.remove("password");

            mockMvc.perform(post("/api/auth/login")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

            verify(authenticationService, org.mockito.Mockito.never()).login(request);
        }

        // Test: Blank email and password returns 400 BAD_REQUEST
        @Test
        @DisplayName("should return bad request for blank email and password")
        void shouldReturnBadRequestForBlankCredentials() throws Exception {
            Map<String, Object> request = Map.of("email", " ", "password", " ");

            mockMvc.perform(post("/api/auth/login")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

            verify(authenticationService, org.mockito.Mockito.never()).login(request);
        }

        // Test: Null request body returns 400 BAD_REQUEST
        @Test
        @DisplayName("should return bad request for null request body")
        void shouldReturnBadRequestForNullLoginBody() throws Exception {
            mockMvc.perform(post("/api/auth/login")
                    .contentType(APPLICATION_JSON)
                    .content("null"))
                .andExpect(status().isBadRequest());

            verify(authenticationService, org.mockito.Mockito.never()).login(null);
        }

        // Test: Malformed JSON returns 400 BAD_REQUEST
        @Test
        @DisplayName("should return bad request for malformed login json")
        void shouldReturnBadRequestForMalformedLoginJson() throws Exception {
            mockMvc.perform(post("/api/auth/login")
                    .contentType(APPLICATION_JSON)
                    .content("{\"email\":"))
                .andExpect(status().isBadRequest());

            verify(authenticationService, org.mockito.Mockito.never()).login(null);
        }

        // Test: Missing Content-Type header returns 415 UNSUPPORTED_MEDIA_TYPE
        @Test
        @DisplayName("should return unsupported media type for login")
        void shouldReturnUnsupportedMediaTypeForLogin() throws Exception {
            mockMvc.perform(post("/api/auth/login")
                    .content("{}"))
                .andExpect(status().isUnsupportedMediaType());

            verify(authenticationService, org.mockito.Mockito.never()).login(null);
        }
    }

    private static Map<String, Object> validRegisterRequest() {
        return new java.util.HashMap<>(Map.of(
            "name", "Asha Raman",
            "email", "asha@northernarc.org",
            "password", "StrongPass@123",
            "phoneNumber", "9000000001"
        ));
    }

    private static Map<String, Object> validLoginRequest() {
        return new java.util.HashMap<>(Map.of(
            "email", "asha@northernarc.org",
            "password", "StrongPass@123"
        ));
    }
}

