package org.northernarc.week5_assess.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.northernarc.week5_assess.entity.Customer;
import org.northernarc.week5_assess.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("CustomerIntegrationTest")
class CustomerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void cleanDb() {
        customerRepository.deleteAll();
    }

    @Nested
    @DisplayName("Customer Registration")
    class RegistrationTests {

        @Test
        @DisplayName("should register customer successfully")
        void shouldRegisterCustomerSuccessfully() throws Exception {
            // Arrange
            Map<String, Object> request = validRegistrationRequest();

            // Act
            String body = mockMvc.perform(post("/api/auth/register")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("asha@northernarc.org"))
                .andReturn()
                .getResponse()
                .getContentAsString();

            // Assert
            JsonNode node = objectMapper.readTree(body);
            Long customerId = node.get("id").asLong();
            assertThat(customerRepository.findById(customerId)).isPresent();
            Customer persisted = customerRepository.findById(customerId).orElseThrow();
            assertThat(persisted.getPassword()).isNotEqualTo("StrongPass@123");
            assertThat(customerRepository.count()).isEqualTo(1);
        }

        @Test
        @DisplayName("should return conflict for duplicate email")
        void shouldReturnConflictForDuplicateEmail() throws Exception {
            // Arrange
            mockMvc.perform(post("/api/auth/register")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRegistrationRequest())))
                .andExpect(status().isCreated());

            // Act + Assert
            mockMvc.perform(post("/api/auth/register")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRegistrationRequest())))
                .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("should return bad request for invalid email")
        void shouldReturnBadRequestForInvalidEmail() throws Exception {
            Map<String, Object> req = validRegistrationRequest();
            req.put("email", "bad");

            mockMvc.perform(post("/api/auth/register")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should return bad request for missing name")
        void shouldReturnBadRequestForMissingName() throws Exception {
            Map<String, Object> req = validRegistrationRequest();
            req.remove("name");

            mockMvc.perform(post("/api/auth/register")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should return bad request for missing password")
        void shouldReturnBadRequestForMissingPassword() throws Exception {
            Map<String, Object> req = validRegistrationRequest();
            req.remove("password");

            mockMvc.perform(post("/api/auth/register")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should return bad request for invalid phone and blank fields")
        void shouldReturnBadRequestForInvalidPhoneAndBlankFields() throws Exception {
            Map<String, Object> req = validRegistrationRequest();
            req.put("phoneNumber", "123");
            req.put("name", " ");

            mockMvc.perform(post("/api/auth/register")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Customer CRUD Flow")
    class CrudFlowTests {

        @Test
        @DisplayName("should perform create retrieve update delete and validate database state")
        void shouldPerformCompleteCrudFlow() throws Exception {
            // Arrange
            Map<String, Object> createReq = validCustomerRequest();

            // Act - create
            String created = mockMvc.perform(post("/api/customers")
                    .with(jwt())
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("asha@northernarc.org"))
                .andReturn().getResponse().getContentAsString();
            long id = objectMapper.readTree(created).get("id").asLong();

            // Assert - persisted
            assertThat(customerRepository.findById(id)).isPresent();

            // Act + Assert - retrieve by id
            mockMvc.perform(get("/api/customers/{id}", id).with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

            // Act + Assert - retrieve all
            mockMvc.perform(get("/api/customers").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(id));

            // Act + Assert - update
            Map<String, Object> updateReq = validCustomerRequest();
            updateReq.put("name", "Asha Updated");
            mockMvc.perform(put("/api/customers/{id}", id)
                    .with(jwt())
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Asha Updated"));

            assertThat(customerRepository.findById(id).orElseThrow().getName()).isEqualTo("Asha Updated");

            // Act + Assert - delete
            mockMvc.perform(delete("/api/customers/{id}", id).with(jwt()))
                .andExpect(status().isNoContent());

            // Assert - deleted customer cannot be retrieved
            mockMvc.perform(get("/api/customers/{id}", id).with(jwt()))
                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("should return empty customer list")
        void shouldReturnEmptyCustomerList() throws Exception {
            customerRepository.deleteAll();

            mockMvc.perform(get("/api/customers").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
        }

        @Test
        @DisplayName("should return 401 for secured endpoint without jwt")
        void shouldReturnUnauthorizedWithoutJwt() throws Exception {
            mockMvc.perform(get("/api/customers"))
                .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("should return not found for unknown customer")
        void shouldReturnNotFoundForUnknownCustomer() throws Exception {
            mockMvc.perform(get("/api/customers/{id}", 9999L).with(jwt()))
                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("should return bad request for invalid customer id")
        void shouldReturnBadRequestForInvalidCustomerId() throws Exception {
            mockMvc.perform(get("/api/customers/{id}", "abc").with(jwt()))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should return bad request for empty body malformed and unsupported media")
        void shouldHandleBodyAndMediaEdgeCases() throws Exception {
            mockMvc.perform(post("/api/customers").with(jwt()).contentType(APPLICATION_JSON).content(""))
                .andExpect(status().isBadRequest());

            mockMvc.perform(post("/api/customers").with(jwt()).contentType(APPLICATION_JSON).content("{\"name\":"))
                .andExpect(status().isBadRequest());

            mockMvc.perform(post("/api/customers").with(jwt()).content("{}"))
                .andExpect(status().isUnsupportedMediaType());
        }

        @Test
        @DisplayName("should support multiple customers in database")
        void shouldSupportMultipleCustomersInDatabase() throws Exception {
            mockMvc.perform(post("/api/customers").with(jwt()).contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validCustomerRequest())))
                .andExpect(status().isCreated());

            Map<String, Object> second = validCustomerRequest();
            second.put("email", "ravi@northernarc.org");
            second.put("phoneNumber", "9000000002");
            second.put("name", "Ravi Kumar");

            mockMvc.perform(post("/api/customers").with(jwt()).contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(second)))
                .andExpect(status().isCreated());

            assertThat(customerRepository.count()).isEqualTo(2);
            mockMvc.perform(get("/api/customers").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
        }
    }

    private static Map<String, Object> validRegistrationRequest() {
        Map<String, Object> request = new HashMap<>();
        request.put("name", "Asha Raman");
        request.put("email", "asha@northernarc.org");
        request.put("password", "StrongPass@123");
        request.put("phoneNumber", "9000000001");
        return request;
    }

    private static Map<String, Object> validCustomerRequest() {
        Map<String, Object> request = new HashMap<>();
        request.put("name", "Asha Raman");
        request.put("email", "asha@northernarc.org");
        request.put("phoneNumber", "9000000001");
        return request;
    }
}
