package org.northernarc.flightapplication.service;

import org.northernarc.flightapplication.dto.FlightRequestDTO;
import org.northernarc.flightapplication.dto.FlightResponseDTO;
import org.northernarc.flightapplication.dto.FlightTicketRequestDTO;
import org.northernarc.flightapplication.dto.FlightTicketResponseDTO;

import java.util.List;

public interface FlightTicketService {
    FlightTicketResponseDTO createFlightTicket(FlightTicketRequestDTO flightTicketRequestDTO);
    FlightTicketResponseDTO findTicketById(Long id);
    List<FlightTicketResponseDTO> findAll();
    FlightTicketResponseDTO updateFlightTicket(Long id, FlightTicketResponseDTO flightTicketRequestDTO);
    void deleteFlightTicket(Long id);
}
