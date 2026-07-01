package org.northernarc.flightapplication.service;

import org.northernarc.flightapplication.dto.FlightRequestDTO;
import org.northernarc.flightapplication.dto.FlightResponseDTO;
import org.northernarc.flightapplication.exception.FlightTicketNotFoundException;
import org.northernarc.flightapplication.model.Flight;
import org.northernarc.flightapplication.repository.FlightRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class FlightServiceImpl implements FlightService {
    @Autowired
    private FlightRepo flightRepo;

    @Override
    public FlightResponseDTO createFlight(FlightRequestDTO flightRequestDTO) {
        Flight flight = mapToEntity(flightRequestDTO);
        flightRepo.save(flight);
        return mapToResponseDTO(flight);
    }

    @Override
    public FlightResponseDTO findFlightById(Long id) {
        return flightRepo.findAll().stream()
                .filter(flight -> flight.getId().equals(id))
                .findFirst()
                .map(this::mapToResponseDTO)
                .orElseThrow(() -> new FlightTicketNotFoundException("Flight not found with id: " + id));
    }

    @Override
    public List<FlightResponseDTO> getAllFlights() {
     return flightRepo.findAll().stream()
             .map(this::mapToResponseDTO)
             .toList();
    }

    @Override
    public void deleteFlight(Long id) {
    flightRepo.findAll().stream()
            .filter(flight -> flight.getId().equals(id))
            .findFirst()
            .ifPresentOrElse(flight -> flightRepo.delete(flight),
                    () -> { throw new FlightTicketNotFoundException("Flight not found with id: " + id); });
    }

    @Override
    public List<FlightResponseDTO> findAll() {
       return flightRepo.findAll().stream().map(this::mapToResponseDTO).toList();
    }

    public Flight mapToEntity(FlightRequestDTO flightRequestDTO) {
        Flight flight = new Flight();
        flight.setSource(flightRequestDTO.getSource());
        flight.setDestination(flightRequestDTO.getDestination());
        flight.setFlightNumber(flightRequestDTO.getFlightNumber());
        flight.setDepartureTime(flightRequestDTO.getDepartureTime());
        flight.setArrivalTime(flightRequestDTO.getArrivalTime());
        flight.setAirLineName(flightRequestDTO.getAirLineName());
        return flight;
    }

    public FlightResponseDTO mapToResponseDTO(Flight flight) {
        FlightResponseDTO fto = new FlightResponseDTO();
        flight.setId(flight.getId());
        flight.setArrivalTime(fto.getArrivalTime());
        flight.setArrivalTime(fto.getArrivalTime());
        flight.setAirLineName(fto.getAirLineName());
        flight.setFlightNumber(fto.getFlightNumber());
        flight.setSource(fto.getSource());
        flight.setDestination(fto.getDestination());
        return fto;
    }
}
