package com.upwork.hometask.demo.models.resp;

import static org.springframework.http.HttpStatus.CREATED;

public class CreatedResponse<T> extends VgpResponse<T> {

  public CreatedResponse(T data) {
    super(data, null, CREATED);
  }
}
