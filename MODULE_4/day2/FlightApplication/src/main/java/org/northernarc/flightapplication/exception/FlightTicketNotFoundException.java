package org.northernarc.flightapplication.exception;

public class FlightTicketNotFoundException extends RuntimeException {
    public FlightTicketNotFoundException(String message) {
        super(message);
    }
}
