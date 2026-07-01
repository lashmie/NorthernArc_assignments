package org.northernarc.flightapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class FlightResponseDTO {
    private Long id;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private String airLineName;
    private String source;
    private String destination;
    private String flightNumber;
}
