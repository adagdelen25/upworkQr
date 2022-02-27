package com.upwork.hometask.demo.models.req;

import java.util.Date;
import lombok.Data;

@Data
public class RequestBody {
  private Date timestamp;
  private String transactionId;
}
