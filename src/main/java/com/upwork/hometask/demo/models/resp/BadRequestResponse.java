package com.upwork.hometask.demo.models.resp;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import java.util.Arrays;
import java.util.List;

public class BadRequestResponse<T> extends VgpResponse<T> {

  public BadRequestResponse(String message) {
    this(Arrays.asList(new ResponseError(message)));
  }

  public BadRequestResponse(List<ResponseError> errors) {
    super(null, errors, BAD_REQUEST);
  }
}
