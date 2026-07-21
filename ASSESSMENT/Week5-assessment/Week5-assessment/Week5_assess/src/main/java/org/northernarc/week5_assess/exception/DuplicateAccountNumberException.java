package org.northernarc.week5_assess.exception;

public class DuplicateAccountNumberException extends RuntimeException {
    public DuplicateAccountNumberException(String message) {
        super(message);
    }
}

