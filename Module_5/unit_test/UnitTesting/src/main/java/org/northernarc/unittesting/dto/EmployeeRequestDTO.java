package org.northernarc.unittesting.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRequestDTO {

	@NotBlank(message = "name is required")
	private String name;

	@NotNull(message = "salary is required")
	@PositiveOrZero(message = "salary must be >= 0")
	private Double salary;
}
