package com.upwork.hometask.demo.models.resp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResponseBody<T> {
  private T data;
  private List<ResponseError> errors;
  private Date timestamp;
  private String transactionId;

  public ResponseBody(T data, List<ResponseError> errors, HttpStatus status) {
    this.data = data;
    this.errors = errors != null ? errors : new ArrayList<>();
    this.timestamp = new Date(System.currentTimeMillis());
  }
}
