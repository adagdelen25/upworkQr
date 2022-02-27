package com.upwork.hometask.demo.models.resp;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import lombok.Getter;

@Getter
public class NoContentResponse extends VgpResponse {
  public NoContentResponse() {
    super(null, null, NO_CONTENT);
  }
}
