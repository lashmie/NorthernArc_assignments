package org.northernarc.flightapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightTicketResponseDTO {
    private Long ticketId;
    private int seatNo;
    private LocalDate bookingDate;
}
