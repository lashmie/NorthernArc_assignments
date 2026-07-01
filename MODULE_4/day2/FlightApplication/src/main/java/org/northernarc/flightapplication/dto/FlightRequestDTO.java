package org.northernarc.flightapplication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightRequestDTO {
    @NotNull(message = "Departure time is required")
    private LocalTime departureTime;

    @NotNull(message = "Arrival time is required")
    private LocalTime arrivalTime;

    @NotBlank(message = "Airline name is required")
    @Size(min = 2, max = 80, message = "Airline name must be between 2 and 80 characters")
    private String airLineName;

    @NotBlank(message = "Source is required")
    @Size(min = 2, max = 80, message = "Source must be between 2 and 80 characters")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Source must contain only letters and spaces")
    private String source;

    @NotBlank(message = "Destination is required")
    @Size(min = 2, max = 80, message = "Destination must be between 2 and 80 characters")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Destination must contain only letters and spaces")
    private String destination;

    @NotBlank(message = "Flight number is required")
    @Pattern(regexp = "^[A-Z0-9-]{3,10}$", message = "Flight number must be 3-10 uppercase letters, numbers, or -")
    private String flightNumber;
}

