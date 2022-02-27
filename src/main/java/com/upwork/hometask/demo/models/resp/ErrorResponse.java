package com.upwork.hometask.demo.models.resp;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import java.util.Arrays;
import java.util.List;

public class ErrorResponse<T> extends VgpResponse {

  public ErrorResponse(String message) {
    this(Arrays.asList(new ResponseError(message)));
  }

  public ErrorResponse(List<ResponseError> errors) {
    super(null, errors, INTERNAL_SERVER_ERROR);
  }
}
