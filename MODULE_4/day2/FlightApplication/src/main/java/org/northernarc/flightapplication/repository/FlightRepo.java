package org.northernarc.flightapplication.repository;

import org.northernarc.flightapplication.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepo extends JpaRepository<Flight,Long> {
}
