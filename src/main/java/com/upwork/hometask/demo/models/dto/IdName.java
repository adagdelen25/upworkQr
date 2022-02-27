package com.upwork.hometask.demo.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IdName<T> {

  private T id;
  private String name;
}
