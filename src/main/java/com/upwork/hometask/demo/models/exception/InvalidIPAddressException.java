package com.upwork.hometask.demo.models.exception;

public class InvalidIPAddressException extends RuntimeException {
    public InvalidIPAddressException() {
        super("Ip is not valid");
    }

    public InvalidIPAddressException(String message) {
        super(message);
    }
}