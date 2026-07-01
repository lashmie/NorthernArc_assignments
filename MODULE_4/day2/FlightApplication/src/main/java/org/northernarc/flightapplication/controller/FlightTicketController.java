package org.northernarc.flightapplication.controller;
import org.northernarc.flightapplication.dto.FlightTicketRequestDTO;
import org.northernarc.flightapplication.dto.FlightTicketResponseDTO;
import org.northernarc.flightapplication.service.FlightTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class FlightTicketController {
    @Autowired
    private FlightTicketService flightTicketService;

    @PostMapping("/create")
    public ResponseEntity<FlightTicketResponseDTO> createTicket(@RequestBody FlightTicketRequestDTO flightTicketRequestDTO) {
        FlightTicketResponseDTO createdTicket = flightTicketService.createFlightTicket(flightTicketRequestDTO);
        return ResponseEntity.ok(createdTicket);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<FlightTicketResponseDTO> getTicketById(@PathVariable Long id) {
        FlightTicketResponseDTO ticket = flightTicketService.findTicketById(id);
        return ResponseEntity.ok(ticket);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<FlightTicketResponseDTO>> getAllTickets() {
        List<FlightTicketResponseDTO> tickets = flightTicketService.findAll();
        return ResponseEntity.ok(tickets);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<FlightTicketResponseDTO> updateTicket(
            @PathVariable Long id,
            @RequestBody FlightTicketResponseDTO flightTicketResponseDTO) {
        FlightTicketResponseDTO updatedTicket = flightTicketService.updateFlightTicket(id, flightTicketResponseDTO);
        return ResponseEntity.ok(updatedTicket);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        flightTicketService.deleteFlightTicket(id);
        return ResponseEntity.noContent().build();
    }
}
