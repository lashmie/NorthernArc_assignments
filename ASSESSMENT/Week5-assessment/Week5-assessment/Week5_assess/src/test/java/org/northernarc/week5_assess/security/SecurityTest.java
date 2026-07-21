package org.northernarc.week5_assess.security;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Security - Integration Tests")
public class SecurityTest {

	@Autowired
	private MockMvc mockMvc;

	@Nested
	@DisplayName("Authentication")
	class AuthenticationTests {
		@Test
		@DisplayName("should allow public auth endpoints without token")
		void publicAuthEndpoint_whenNoJwt_shouldBeAccessible() throws Exception {
			mockMvc.perform(post("/auth/login")
					.contentType(APPLICATION_JSON)
					.content("{\"email\":\"a@a.com\",\"password\":\"x\"}"))
				.andExpect(status().is4xxClientError());
		}

		@Test
		@DisplayName("should deny protected endpoint when JWT missing")
		void protectedEndpoint_whenJwtMissing_shouldReturnUnauthorized() throws Exception {
			mockMvc.perform(get("/customers/1"))
				.andExpect(status().isUnauthorized());
		}

		@Test
		@DisplayName("should allow protected endpoint when JWT present")
		void protectedEndpoint_whenJwtPresent_shouldNotReturnUnauthorized() throws Exception {
			mockMvc.perform(get("/customers/1")
					.with(jwt().jwt(j -> j.claim("scope", "ROLE_USER"))))
				.andExpect(status().is4xxClientError());
		}
	}

	@Nested
	@DisplayName("Authorization")
	class AuthorizationTests {
		@Test
		@DisplayName("should deny admin-only endpoint for user role")
		void adminEndpoint_whenRoleIsUser_shouldReturnForbidden() throws Exception {
			mockMvc.perform(post("/accounts")
					.with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
					.contentType(APPLICATION_JSON)
					.content("{\"customerId\":1,\"accountType\":\"SAVINGS\"}"))
				.andExpect(status().is4xxClientError());
		}
	}
}
