package org.northernarc.webapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmployeeRequestDto {
	@NotBlank(message = "Name is required")
	private String name;

	@NotBlank(message = "Email is required")
	@Email(message = "Please provide a valid email")
	private String email;

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getEmail() {
				return email;
			}

			public void setEmail(String email) {
				this.email = email;
			}
}
