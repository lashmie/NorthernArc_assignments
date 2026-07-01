package org.northernarc.flightapplication.repository;

import org.northernarc.flightapplication.model.FlightTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightTicketRepo extends JpaRepository<FlightTicket, Long> {
}
