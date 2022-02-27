package com.upwork.hometask.demo.models.resp;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import java.util.Arrays;
import java.util.List;

public class ValidationErrorResponse<T> extends VgpResponse<T> {

  public ValidationErrorResponse(String message) {
    this(Arrays.asList(new ResponseError(message)));
  }

  public ValidationErrorResponse(String message, String field) {
    this(Arrays.asList(new ResponseError(message, field)));
  }

  public ValidationErrorResponse(List<ResponseError> errors) {
    super(null, errors, NOT_ACCEPTABLE);
  }
}
