
package org.northernarc.flightapplication.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightTicketRequestDTO {
	@NotNull(message = "Seat number is required")
	@Min(value = 1, message = "Seat number must be at least 1")
	@Max(value = 999, message = "Seat number must be less than 1000")
	private Integer seatNo;

	@NotNull(message = "Booking date is required")
	@FutureOrPresent(message = "Booking date must be today or a future date")
	private LocalDate bookingDate;

	@NotNull(message = "Customer id is required")
	@Positive(message = "Customer id must be positive")
	private Long customerId;

	@NotNull(message = "Flight id is required")
	@Positive(message = "Flight id must be positive")
	private Long flightId;
}

