package org.northernarc.productrental.exception;

public class RentalRecordNotFoundException extends RuntimeException {

    public RentalRecordNotFoundException(String message) {
        super(message);
    }
}

