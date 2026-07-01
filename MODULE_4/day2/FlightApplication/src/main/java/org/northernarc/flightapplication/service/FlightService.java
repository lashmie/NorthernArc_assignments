package org.northernarc.flightapplication.service;

import org.northernarc.flightapplication.dto.FlightRequestDTO;
import org.northernarc.flightapplication.dto.FlightResponseDTO;

import java.util.List;

public interface FlightService {
    FlightResponseDTO createFlight(FlightRequestDTO flightRequestDTO);
    FlightResponseDTO findFlightById(Long id);
    List<FlightResponseDTO> getAllFlights();
    void deleteFlight(Long id);
    List<FlightResponseDTO> findAll();
}
