package org.northernarc.ecom.exceptions;

public class ProductNotFound extends RuntimeException{
    public ProductNotFound(String message) {
        super(message);
    }
}
