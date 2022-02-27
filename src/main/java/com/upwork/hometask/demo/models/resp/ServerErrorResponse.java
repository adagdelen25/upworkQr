package com.upwork.hometask.demo.models.resp;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import java.util.Arrays;
import java.util.List;

public class ServerErrorResponse<T> extends VgpResponse<T> {

  public ServerErrorResponse(String message) {
    this(Arrays.asList(new ResponseError(message)));
  }

  public ServerErrorResponse(List<ResponseError> errors) {
    super(null, errors, INTERNAL_SERVER_ERROR);
  }
}
