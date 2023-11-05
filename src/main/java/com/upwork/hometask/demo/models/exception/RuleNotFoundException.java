package com.upwork.hometask.demo.models.exception;

public class RuleNotFoundException extends RuntimeException {
  public RuleNotFoundException(String message) {
    super(message);
  }
}
