package org.northernarc.week5_assess.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.northernarc.week5_assess.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * CustomerControllerTest - Comprehensive MockMvc Tests for Customer Management API
 *
 * TESTING COVERAGE:
 * ================
 * 1. CREATE CUSTOMER (POST /api/customers):
 *    - Happy Path: Valid request -> 201 CREATED + customer details
 *      Verifies: verify(customerService).createCustomer(any())
 *    - Duplicate Email: 409 CONFLICT
 *      Verifies: verify(customerService).createCustomer(any())
 *    - Validation Failures -> 400 BAD_REQUEST with verifyNoInteractions(customerService)
 *      * Invalid email format
 *      * Blank name
 *      * Invalid phone length
 *      * Missing body
 *      * Malformed JSON
 *    - Media Type: 415 UNSUPPORTED_MEDIA_TYPE with verifyNoInteractions
 *
 * 2. LIST ALL CUSTOMERS (GET /api/customers):
 *    - Happy Path: Retrieve all customers -> 200 OK + List of customers
 *      Verifies: verify(customerService).getAllCustomers()
 *    - Empty List: No customers -> 200 OK + empty array
 *      Verifies: verify(customerService).getAllCustomers()
 *
 * 3. GET CUSTOMER BY ID (GET /api/customers/{id}):
 *    - Happy Path: Valid ID -> 200 OK + customer details
 *      Verifies: verify(customerService).getCustomerById(1L)
 *    - Not Found: Invalid ID (404) -> 404 NOT_FOUND
 *      Verifies: verify(customerService).getCustomerById(404L)
 *    - Invalid Path Variable: Non-numeric ID -> 400 BAD_REQUEST with verifyNoInteractions
 *
 * 4. UPDATE CUSTOMER (PUT /api/customers/{id}):
 *    - Happy Path: Valid request -> 200 OK + updated customer
 *      Verifies: verify(customerService).updateCustomer(eq(1L), any())
 *    - Not Found: Invalid customer ID (404)
 *      Verifies: verify(customerService).updateCustomer(eq(404L), any())
 *    - Duplicate Email: 409 CONFLICT
 *      Verifies: verify(customerService).updateCustomer(eq(1L), any())
 *    - Validation Failures -> 400 BAD_REQUEST with verifyNoInteractions(customerService)
 *      * Invalid email format
 *      * Invalid path variable (non-numeric ID)
 *      * Malformed JSON
 *    - Media Type: 415 UNSUPPORTED_MEDIA_TYPE with verifyNoInteractions
 *
 * 5. DELETE CUSTOMER (DELETE /api/customers/{id}):
 *    - Happy Path: Valid ID -> 204 NO_CONTENT
 *      Verifies: verify(customerService).deleteCustomer(1L)
 *    - Not Found: Invalid ID (404) -> 404 NOT_FOUND
 *      Verifies: verify(customerService).deleteCustomer(404L)
 *    - Invalid Path Variable: Non-numeric ID -> 400 BAD_REQUEST with verifyNoInteractions
 *
 * KEY VERIFICATION PATTERNS:
 * =========================
 * - verify(customerService).method(...) -> Service called exactly once
 * - verifyNoInteractions(customerService) -> Service NEVER called for invalid input
 *   Used for: malformed JSON, missing body, unsupported media type, invalid path variables
 *
 * BAD REQUEST BEHAVIOR (verifyNoInteractions enforced):
 * =====================================================
 * Missing values or invalid formats must:
 * 1. Return 400 BAD_REQUEST at the validation/binding layer
 * 2. NEVER reach the service layer
 * 3. This is verified by: verifyNoInteractions(customerService)
 */
@WebMvcTest(CustomerController.class)
@DisplayName("CustomerControllerTest")
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    // Setup method called once before all tests in this class
    @BeforeAll
    static void beforeAll() {
        // One-time initialization before any test runs
    }

    // Setup method called before each test method
    @BeforeEach
    void beforeEach() {
        // Reset mock state and clear any previous interactions with customerService
        reset(customerService);
    }

    // Cleanup method called after each test method
    @AfterEach
    void afterEach() {
        // Verify no unexpected interactions with mocks after each test
        verifyNoMoreInteractions(customerService);
    }

    // Cleanup method called once after all tests in this class
    @AfterAll
    static void afterAll() {
        // One-time cleanup after all tests complete
    }

    @Nested
    @DisplayName("POST /api/customers")
    class CreateTests {

        // Test: Valid customer request returns 201 CREATED
        @Test
        @DisplayName("should create customer successfully")
        void shouldCreateCustomerSuccessfully() throws Exception {
            Map<String, Object> request = validCustomerRequest();
            Map<String, Object> response = Map.of("id", 11L, "name", "Asha", "email", "asha@northernarc.org", "phoneNumber", "9000000001");
            org.mockito.Mockito.when(customerService.createCustomer(request))
                .thenReturn(response);

            mockMvc.perform(post("/api/customers")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(11L))
                .andExpect(jsonPath("$.email").value("asha@northernarc.org"));

            verify(customerService, times(1)).createCustomer(request);
        }

        // Test: Duplicate email returns 409 CONFLICT
        @Test
        @DisplayName("should return conflict for duplicate email")
        void shouldReturnConflictForDuplicateEmail() throws Exception {
            Map<String, Object> req = validCustomerRequest();
            org.mockito.Mockito.when(customerService.createCustomer(req))
                .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "duplicate email"));

            mockMvc.perform(post("/api/customers")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict());

            verify(customerService, times(1)).createCustomer(req);
        }

        // Test: Invalid email format returns 400 BAD_REQUEST
        @Test
        @DisplayName("should return bad request for invalid email")
        void shouldReturnBadRequestForInvalidEmail() throws Exception {
            Map<String, Object> req = validCustomerRequest();
            req.put("email", "invalid");

            mockMvc.perform(post("/api/customers")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());

            verify(customerService, times(0)).createCustomer(req);
        }

        // Test: Blank name returns 400 BAD_REQUEST
        @Test
        @DisplayName("should return bad request for blank name")
        void shouldReturnBadRequestForBlankName() throws Exception {
            Map<String, Object> req = validCustomerRequest();
            req.put("name", " ");

            mockMvc.perform(post("/api/customers")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());

            verify(customerService, times(0)).createCustomer(req);
        }

        // Test: Invalid phone returns 400 BAD_REQUEST
        @Test
        @DisplayName("should return bad request for invalid phone")
        void shouldReturnBadRequestForInvalidPhone() throws Exception {
            Map<String, Object> req = validCustomerRequest();
            req.put("phoneNumber", "1234");

            mockMvc.perform(post("/api/customers")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());

            verify(customerService, times(0)).createCustomer(req);
        }

        // Test: Missing body returns 400 BAD_REQUEST
        @Test
        @DisplayName("should return bad request for missing request body")
        void shouldReturnBadRequestForMissingBody() throws Exception {
            mockMvc.perform(post("/api/customers")
                    .contentType(APPLICATION_JSON)
                    .content(""))
                .andExpect(status().isBadRequest());

            verify(customerService, times(0)).createCustomer(null);
        }

        // Test: Malformed JSON returns 400 BAD_REQUEST
        @Test
        @DisplayName("should return bad request for malformed json")
        void shouldReturnBadRequestForMalformedJson() throws Exception {
            mockMvc.perform(post("/api/customers")
                    .contentType(APPLICATION_JSON)
                    .content("{\"name\":"))
                .andExpect(status().isBadRequest());

            verify(customerService, times(0)).createCustomer(null);
        }

        // Test: Missing Content-Type returns 415 UNSUPPORTED_MEDIA_TYPE
        @Test
        @DisplayName("should return unsupported media type")
        void shouldReturnUnsupportedMediaType() throws Exception {
            mockMvc.perform(post("/api/customers")
                    .content(objectMapper.writeValueAsString(validCustomerRequest())))
                .andExpect(status().isUnsupportedMediaType());

            verify(customerService, times(0)).createCustomer(null);
        }
    }

    @Nested
    @DisplayName("GET /api/customers")
    class ListTests {

        // Test: Returns list of all customers
        @Test
        @DisplayName("should return customer list")
        void shouldReturnCustomerList() throws Exception {
            org.mockito.Mockito.when(customerService.getAllCustomers())
                .thenReturn(List.of(
                    Map.of("id", 1L, "name", "Asha"),
                    Map.of("id", 2L, "name", "Ravi")
                ));

            mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));

            verify(customerService, times(1)).getAllCustomers();
        }

        // Test: Returns empty list
        @Test
        @DisplayName("should return empty list")
        void shouldReturnEmptyList() throws Exception {
            org.mockito.Mockito.when(customerService.getAllCustomers()).thenReturn(List.of());

            mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

            verify(customerService, times(1)).getAllCustomers();
        }
    }

    @Nested
    @DisplayName("GET /api/customers/{id}")
    class GetByIdTests {

        // Test: Valid customer ID returns 200 OK
        @Test
        @DisplayName("should return existing customer")
        void shouldReturnExistingCustomer() throws Exception {
            org.mockito.Mockito.when(customerService.getCustomerById(1L))
                .thenReturn(Map.of("id", 1L, "name", "Asha", "email", "asha@northernarc.org"));

            mockMvc.perform(get("/api/customers/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("asha@northernarc.org"));

            verify(customerService, times(1)).getCustomerById(1L);
        }

        // Test: Invalid customer ID returns 404 NOT_FOUND
        @Test
        @DisplayName("should return not found for unknown customer")
        void shouldReturnNotFoundForUnknownCustomer() throws Exception {
            org.mockito.Mockito.when(customerService.getCustomerById(404L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "not found"));

            mockMvc.perform(get("/api/customers/{id}", 404L))
                .andExpect(status().isNotFound());

            verify(customerService, times(1)).getCustomerById(404L);
        }

        // Test: Invalid path variable returns 400 BAD_REQUEST
        @Test
        @DisplayName("should return bad request for invalid path variable")
        void shouldReturnBadRequestForInvalidPathVariable() throws Exception {
            mockMvc.perform(get("/api/customers/{id}", "abc"))
                .andExpect(status().isBadRequest());

            verify(customerService, times(0)).getCustomerById(0L);
        }
    }

    @Nested
    @DisplayName("PUT /api/customers/{id}")
    class UpdateTests {

        // Test: Valid update returns 200 OK
        @Test
        @DisplayName("should update customer successfully")
        void shouldUpdateCustomerSuccessfully() throws Exception {
            Map<String, Object> request = validCustomerRequest();
            org.mockito.Mockito.when(customerService.updateCustomer(eq(1L), eq(request)))
                .thenReturn(Map.of("id", 1L, "name", "Updated"));

            mockMvc.perform(put("/api/customers/{id}", 1L)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));

            verify(customerService, times(1)).updateCustomer(eq(1L), eq(request));
        }

        // Test: Unknown customer returns 404 NOT_FOUND
        @Test
        @DisplayName("should return not found for unknown customer on update")
        void shouldReturnNotFoundForUnknownCustomerOnUpdate() throws Exception {
            Map<String, Object> request = validCustomerRequest();
            org.mockito.Mockito.when(customerService.updateCustomer(eq(404L), eq(request)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "not found"));

            mockMvc.perform(put("/api/customers/{id}", 404L)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

            verify(customerService, times(1)).updateCustomer(eq(404L), eq(request));
        }

        // Test: Duplicate email returns 409 CONFLICT
        @Test
        @DisplayName("should return conflict for duplicate email on update")
        void shouldReturnConflictForDuplicateEmailOnUpdate() throws Exception {
            Map<String, Object> request = validCustomerRequest();
            org.mockito.Mockito.when(customerService.updateCustomer(eq(1L), eq(request)))
                .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "duplicate email"));

            mockMvc.perform(put("/api/customers/{id}", 1L)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

            verify(customerService, times(1)).updateCustomer(eq(1L), eq(request));
        }

        // Test: Invalid email returns 400 BAD_REQUEST
        @Test
        @DisplayName("should return bad request for invalid update request")
        void shouldReturnBadRequestForInvalidUpdateRequest() throws Exception {
            Map<String, Object> req = validCustomerRequest();
            req.put("email", "bad");

            mockMvc.perform(put("/api/customers/{id}", 1L)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());

            verify(customerService, times(0)).updateCustomer(eq(1L), eq(req));
        }

        // Test: Invalid path variable returns 400 BAD_REQUEST
        @Test
        @DisplayName("should return bad request for invalid path variable on update")
        void shouldReturnBadRequestForInvalidPathVariableOnUpdate() throws Exception {
            mockMvc.perform(put("/api/customers/{id}", "abc")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validCustomerRequest())))
                .andExpect(status().isBadRequest());

            verify(customerService, times(0)).updateCustomer(eq(0L), eq(validCustomerRequest()));
        }

        // Test: Malformed JSON returns 400 BAD_REQUEST
        @Test
        @DisplayName("should return bad request for malformed json on update")
        void shouldReturnBadRequestForMalformedJsonOnUpdate() throws Exception {
            mockMvc.perform(put("/api/customers/{id}", 1L)
                    .contentType(APPLICATION_JSON)
                    .content("{\"name\":"))
                .andExpect(status().isBadRequest());

            verify(customerService, times(0)).updateCustomer(eq(1L), eq(null));
        }

        // Test: Missing Content-Type returns 415 UNSUPPORTED_MEDIA_TYPE
        @Test
        @DisplayName("should return unsupported media type on update")
        void shouldReturnUnsupportedMediaTypeOnUpdate() throws Exception {
            mockMvc.perform(put("/api/customers/{id}", 1L)
                    .content(objectMapper.writeValueAsString(validCustomerRequest())))
                .andExpect(status().isUnsupportedMediaType());

            verify(customerService, times(0)).updateCustomer(eq(1L), eq(null));
        }
    }

    @Nested
    @DisplayName("DELETE /api/customers/{id}")
    class DeleteTests {

        @Test
        @DisplayName("should delete successfully")
        void shouldDeleteSuccessfully() throws Exception {
            mockMvc.perform(delete("/api/customers/{id}", 1L))
                .andExpect(status().isNoContent());

            verify(customerService).deleteCustomer(1L);
        }

        @Test
        @DisplayName("should return not found for unknown customer on delete")
        void shouldReturnNotFoundForUnknownCustomerOnDelete() throws Exception {
            org.mockito.BDDMockito.willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "not found"))
                .given(customerService).deleteCustomer(404L);

            mockMvc.perform(delete("/api/customers/{id}", 404L))
                .andExpect(status().isNotFound());

            verify(customerService).deleteCustomer(404L);
        }

        @Test
        @DisplayName("should return bad request for invalid id on delete")
        void shouldReturnBadRequestForInvalidIdOnDelete() throws Exception {
            mockMvc.perform(delete("/api/customers/{id}", "abc"))
                .andExpect(status().isBadRequest());

            verifyNoInteractions(customerService);
        }
    }

    private static Map<String, Object> validCustomerRequest() {
        return new java.util.HashMap<>(Map.of(
            "name", "Asha Raman",
            "email", "asha@northernarc.org",
            "phoneNumber", "9000000001"
        ));
    }
}
