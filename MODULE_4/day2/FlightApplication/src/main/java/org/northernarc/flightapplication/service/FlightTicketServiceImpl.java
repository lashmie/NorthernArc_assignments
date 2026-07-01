package org.northernarc.flightapplication.service;

import org.northernarc.flightapplication.dto.FlightTicketRequestDTO;
import org.northernarc.flightapplication.dto.FlightTicketResponseDTO;
import org.northernarc.flightapplication.exception.FlightTicketNotFoundException;
import org.northernarc.flightapplication.model.FlightTicket;
import org.northernarc.flightapplication.repository.FlightTicketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightTicketServiceImpl implements FlightTicketService {
    @Autowired
    private FlightTicketRepo flightTicketRepo;

    @Override
    public FlightTicketResponseDTO createFlightTicket(FlightTicketRequestDTO flightTicketRequestDTO) {
        FlightTicket ticket = mapToEntity(flightTicketRequestDTO);
        FlightTicket savedTicket = flightTicketRepo.save(ticket);
        return mapToDTO(savedTicket);
    }

    @Override
    public FlightTicketResponseDTO findTicketById(Long id) {
        FlightTicket ticket = flightTicketRepo.findById(id)
                .orElseThrow(() -> new FlightTicketNotFoundException("Ticket not found with id: " + id));
        return mapToDTO(ticket);
    }

    @Override
    public List<FlightTicketResponseDTO> findAll() {
        return flightTicketRepo.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public FlightTicketResponseDTO updateFlightTicket(Long id, FlightTicketResponseDTO flightTicketRequestDTO) {
        FlightTicket ticket = flightTicketRepo.findById(id)
                .orElseThrow(() -> new FlightTicketNotFoundException("Ticket not found with id: " + id));

        ticket.setSeatNo(flightTicketRequestDTO.getSeatNo());
        ticket.setBookingDate(flightTicketRequestDTO.getBookingDate());

        FlightTicket updatedTicket = flightTicketRepo.save(ticket);
        return mapToDTO(updatedTicket);
    }

    @Override
    public void deleteFlightTicket(Long id) {
        if (!flightTicketRepo.existsById(id)) {
            throw new FlightTicketNotFoundException("Ticket not found with id: " + id);
        }
        flightTicketRepo.deleteById(id);
    }

    public FlightTicket mapToEntity(FlightTicketRequestDTO flightTicketRequestDTO){
        FlightTicket flightTicket = new FlightTicket();
        flightTicket.setSeatNo(flightTicketRequestDTO.getSeatNo());
        flightTicket.setBookingDate(flightTicketRequestDTO.getBookingDate());
        return flightTicket;
    }

    public FlightTicketResponseDTO mapToDTO(FlightTicket flightTicket) {
        FlightTicketResponseDTO dto = new FlightTicketResponseDTO();
        dto.setTicketId(flightTicket.getTicketId());
        dto.setSeatNo(flightTicket.getSeatNo());
        dto.setBookingDate(flightTicket.getBookingDate());
        return dto;
    }
}
