package com.upwork.hometask.demo.models.resp;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import java.util.Arrays;
import java.util.List;

public class NotFoundResponse<T> extends VgpResponse<T> {

  public NotFoundResponse(String message) {
    this(Arrays.asList(new ResponseError(message)));
  }

  public NotFoundResponse(List<ResponseError> errors) {
    super(null, errors, NOT_FOUND);
  }
}
