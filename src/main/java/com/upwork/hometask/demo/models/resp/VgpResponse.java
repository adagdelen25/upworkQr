package com.upwork.hometask.demo.models.resp;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class VgpResponse<T> extends ResponseEntity<ResponseBody<T>> {
  public VgpResponse(T data, List<ResponseError> errors, HttpStatus status) {
    super(new ResponseBody<T>(data, errors, status), status);
  }
}
