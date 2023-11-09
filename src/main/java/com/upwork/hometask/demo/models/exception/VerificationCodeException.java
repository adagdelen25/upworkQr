package com.upwork.hometask.demo.models.exception;

public class VerificationCodeException extends RuntimeException {
    public VerificationCodeException() {
        super("This token is not valid");
    }

    public VerificationCodeException(String message) {
        super(message);
    }
}