package org.northernarc.ecom.exceptions;

public class CustomerNotFound extends RuntimeException{
    public CustomerNotFound(String message) {
        super(message);
    }
}
