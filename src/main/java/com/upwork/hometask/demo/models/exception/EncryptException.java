package com.upwork.hometask.demo.models.exception;

public class EncryptException extends RuntimeException {
    public EncryptException() {
        super("Encrypt is not valid");
    }

    public EncryptException(String message) {
        super(message);
    }
}