package org.northernarc.week5_assess.exception;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.northernarc.week5_assess.controller.CustomerController;
import org.northernarc.week5_assess.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = CustomerController.class)
@DisplayName("GlobalExceptionHandler - MockMvc Tests")
public class GlobalExceptionHandlerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CustomerService customerService;

	@Nested
	@DisplayName("Validation and JSON parsing")
	class ValidationTests {
		@Test
		@DisplayName("should return 400 for malformed JSON")
		void malformedJson_shouldReturnBadRequest() throws Exception {
			mockMvc.perform(post("/customers")
					.contentType(APPLICATION_JSON)
					.content("{\"name\":"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400));
		}
	}

	@Nested
	@DisplayName("Domain exceptions")
	class DomainExceptionTests {
		@Test
		@DisplayName("should map customer not found to 404")
		void customerNotFound_shouldReturnNotFound() throws Exception {
			org.mockito.Mockito.when(customerService.getCustomerById(404L))
				.thenThrow(new CustomerNotFoundException("Customer 404 not found"));

			mockMvc.perform(get("/customers/{id}", 404L))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").exists());
		}

		@Test
		@DisplayName("should map duplicate resource to 409")
		void duplicate_shouldReturnConflict() throws Exception {
			org.mockito.Mockito.when(customerService.getCustomerById(1L))
				.thenThrow(new DuplicateResourceException("Duplicate"));

			mockMvc.perform(get("/customers/{id}", 1L))
				.andExpect(status().isConflict());
		}
	}

	@Nested
	@DisplayName("Security exceptions")
	class SecurityExceptionTests {
		@Test
		@DisplayName("should map bad credentials to 401")
		void badCredentials_shouldReturnUnauthorized() throws Exception {
			org.mockito.Mockito.when(customerService.getCustomerById(1L))
				.thenThrow(new BadCredentialsException("Bad credentials"));

			mockMvc.perform(get("/customers/{id}", 1L))
				.andExpect(status().isUnauthorized());
		}
	}

	@Nested
	@DisplayName("Fallback")
	class FallbackTests {
		@Test
		@DisplayName("should return 500 for unexpected exception")
		void unexpectedException_shouldReturnInternalServerError() throws Exception {
			org.mockito.Mockito.when(customerService.getCustomerById(1L))
				.thenThrow(new RuntimeException("Unexpected"));

			mockMvc.perform(get("/customers/{id}", 1L))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status").value(500));
		}
	}
}
