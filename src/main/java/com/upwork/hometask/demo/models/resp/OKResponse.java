package com.upwork.hometask.demo.models.resp;

import static org.springframework.http.HttpStatus.OK;

public class OKResponse<T> extends VgpResponse<T> {
  public OKResponse(T data) {
    super(data, null, OK);
  }
}
