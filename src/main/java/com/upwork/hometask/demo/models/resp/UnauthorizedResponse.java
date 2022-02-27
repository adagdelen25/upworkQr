package com.upwork.hometask.demo.models.resp;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import java.util.Arrays;
import java.util.List;

public class UnauthorizedResponse<T> extends VgpResponse<T> {

  public UnauthorizedResponse(String message) {
    this(Arrays.asList(new ResponseError(message)));
  }

  public UnauthorizedResponse(List<ResponseError> errors) {
    super(null, errors, UNAUTHORIZED);
  }
}
